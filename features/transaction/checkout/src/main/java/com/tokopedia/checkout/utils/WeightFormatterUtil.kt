package com.tokopedia.checkout.utils

import java.math.BigDecimal
import java.math.RoundingMode

object WeightFormatterUtil {

    private const val KILOGRAM_DIVIDER = 1000.0f
    private const val DIGIT_AFTER_COMMA = 2
    private const val LABEL_KILOGRAM = " kg"
    private const val LABEL_GRAM = " gr"

    @JvmStatic
    fun getFormattedWeight(weight: Double, qty: Int): String {
        val weighTotalFormatted: String
        var weightTotal = weight * qty
        if (weightTotal >= KILOGRAM_DIVIDER) {
            var bigDecimal = BigDecimal(weightTotal / KILOGRAM_DIVIDER)
            bigDecimal = bigDecimal.setScale(DIGIT_AFTER_COMMA, RoundingMode.HALF_UP)
            weightTotal = bigDecimal.toDouble()
            weighTotalFormatted = "$weightTotal $LABEL_KILOGRAM"
        } else {
            weighTotalFormatted = weightTotal.toInt().toString() + LABEL_GRAM
        }
        return weighTotalFormatted.replace(".0 ", "")
            .replace("  ", " ")
    }
}
