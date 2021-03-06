package pw.aru.libs.dicenotation;

import pw.aru.libs.dicenotation.evaluator.DiceEvaluatorBuilder;
import pw.aru.libs.dicenotation.evaluator.DiceSolver;
import pw.aru.libs.dicenotation.lexer.DiceLexer;
import pw.aru.libs.dicenotation.parser.DiceParser;

import java.util.Random;

/**
 * Simple Dice Notation implementation, with some predefined values and functions.
 */
public class SimpleDice {
    protected DiceEvaluatorBuilder builder;
    protected Random r = new Random();

    public SimpleDice() {
        builder = new DiceEvaluatorBuilder()
            .value("pi", Math.PI)
            .value("e", Math.E)
            .value("r", Math::random)
            .valueAlias("r", "rand", "rdn", "random")
            .function("sin", args -> Math.sin(args[0].doubleValue()))
            .function("cos", args -> Math.cos(args[0].doubleValue()))
            .function("tan", args -> Math.tan(args[0].doubleValue()))
            .function("random", args -> roll(args[0].intValue()))
            .functionAlias("random", "rand", "rdn", "r");
    }

    /**
     * Returns the {@link DiceEvaluatorBuilder} of this instance, which can be used for further modifications.
     *
     * @return this instance's {@link DiceEvaluatorBuilder}.
     */
    public DiceEvaluatorBuilder builder() {
        return builder;
    }

    /**
     * Resolves a dice notation string to a number, which can be a {@link Double} or a {@link Integer}.
     *
     * @param s a piece of math, in dice notation.
     * @return the calculated number.
     */
    public Number resolve(String s) {
        return new DiceParser(new DiceLexer(s))
            .parse()
            .accept(new DiceSolver(this::roll).andFinally(builder.build()));
    }

    protected int roll(int sides) {
        return r.nextInt(sides) + 1;
    }
}
