package lambdas;

import java.io.*;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

public class NumberValidator {
    private BufferedReader reader;
    private final Writer writer;

    public NumberValidator(Reader reader, Writer writer) {
        this.reader = new BufferedReader(reader);
        this.writer = writer;
    }

    public static void main(String... args) {
        NumberValidator numberValidator = new NumberValidator(new InputStreamReader(System.in),
                new OutputStreamWriter(System.out));
        runApp(numberValidator);
    }

    public int promptUntilValidMenuChoice() {
        Function<String, Integer> formatNumber = input -> parseToInt(input);
        Function<String, Boolean> invalidInput = input -> invalidMenuChoice(input);
        Function<Void, Void> prompt = prompts -> {
            promptForMenuChoice();
            return null;
        };

        return validatingPrompt(formatNumber, prompt, invalidInput);
    }

    public int promptUntilValidNumberEntered() {
        Function<String, Integer> formatNumber = input -> parseToInt(input);

        Function<String, Boolean> invalidInput = input -> invalid(input);

        Function<Void, Void> prompt = prompts -> {
            promptForNumber();
            return null;
        };

        return validatingPrompt(formatNumber, prompt, invalidInput);
    }

    private int validatingPrompt(Function<String, Integer> formatted,
                                 Function<Void, Void> prompt,
                                 Function<String, Boolean> condition) {

        prompt.apply(null);
        String input = readInput();

        while (condition.apply(input)) {
            prompt.apply(null);
            input = readInput();
        }

        return formatted.apply(input);
    }

    private void promptForMenuChoice() {
        write("Please choose:\n(1) Validate number is between 0 and 100\n(2) Exit");
    }


    private void write(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error when writing message");
        }
    }

    private String readInput() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error reading input");
        }
    }

    private boolean invalidMenuChoice(String input) {
        if (!validMenuChoice(input)) {
            write("Not a valid menu option!");
            return true;
        }
        return false;
    }

    private boolean validMenuChoice(String input) {
        return input.equals("1") || input.equals("2");
    }

    private int parseToInt(String input) {
        return parseInt(input);
    }

    private void promptForNumber() {
        write("Please enter a number:");
    }

    private boolean invalid(String input) {
        if (!isNumber(input)) {
            write("Not a number!");
            return true;
        }

        if (!isInRange(input)) {
            write("Not in range!");
            return true;
        }
        return !isNumber(input);
    }

    private boolean isInRange(String input) {
        int number = parseToInt(input);
        return number >= 0 && number <= 100;
    }

    private boolean isNumber(String input) {
        try {
            parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void runApp(NumberValidator numberValidator) {
        numberValidator.promptUntilValidMenuChoice();
        numberValidator.promptUntilValidNumberEntered();
    }
}
