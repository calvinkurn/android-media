package com.tokopedia.checkout.utils;

import java.math.BigDecimal;

/**
 * Created by Irfan Khoirul on 30/07/18.
 */

public class WeightFormatterUtil {

    private static final float KILOGRAM_DIVIDER = 1000.0f;
    private static final int DIGIT_AFTER_COMMA = 2;
    private static final String LABEL_KILOGRAM = " kg";
    private static final String LABEL_GRAM = " gr";

    public static String getFormattedWeight(double weight, int qty) {
        String weighTotalFormatted;
        double weightTotal = weight * qty;
        if (weightTotal >= KILOGRAM_DIVIDER) {
            BigDecimal bigDecimal = new BigDecimal(weightTotal / KILOGRAM_DIVIDER);
            bigDecimal = bigDecimal.setScale(DIGIT_AFTER_COMMA, BigDecimal.ROUND_HALF_UP);
            weightTotal = bigDecimal.doubleValue();
            weighTotalFormatted = weightTotal + " " + LABEL_KILOGRAM;
        } else {
            weighTotalFormatted = ((int) weightTotal) + LABEL_GRAM;
        }

        return weighTotalFormatted.replace(".0 ", "")
                .replace("  ", " ");
    }

}
