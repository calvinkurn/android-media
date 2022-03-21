package com.tokopedia.vouchercreation.common.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import javax.inject.Inject

class CompactNumberFormatter @Inject constructor() {

    companion object {
        private const val THOUSAND = 1_000
        private const val MILLION = 1_000_000
        private const val PLACEHOLDER_THOUSAND = "%s rb"
        private const val PLACEHOLDER_MILLION = "%s jt"
    }

    enum class OutputDecimalFormat(val pattern: String) {
        ONE_DIGIT_AFTER_COMMA("#.0"),
        TWO_DIGIT_AFTER_COMMA("#.00")
    }

    fun format(
        number: Int,
        pattern: OutputDecimalFormat = OutputDecimalFormat.ONE_DIGIT_AFTER_COMMA
    ): String {
        val decimalFormatter = DecimalFormat(pattern.pattern)

        //To use comma as decimal separator. Ex: 1.3 -> 1,3
        val symbols = DecimalFormatSymbols.getInstance()
        symbols.decimalSeparator = ','
        decimalFormatter.decimalFormatSymbols = symbols

        return when {
            number < THOUSAND -> number.toString()
            number >= MILLION -> {
                val roundedNumber = (number.toFloat() / MILLION)
                val formattedNumber = decimalFormatter.format(roundedNumber)
                String.format(PLACEHOLDER_MILLION, formattedNumber)
            }
            number >= THOUSAND -> {
                val roundedNumber = (number.toFloat() / THOUSAND)
                val formattedNumber = decimalFormatter.format(roundedNumber)
                String.format(PLACEHOLDER_THOUSAND, formattedNumber)
            }
            else -> number.toString()
        }
    }
}