package com.tokopedia.feedcomponent.util.util

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

val IDRLocale = NumberFormat.getCurrencyInstance(Locale("in", "id"))
val decimalFormat = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale("in", "id")))

fun Number.getCurrencyFormatted(): String {
    IDRLocale.maximumFractionDigits = 0
    return IDRLocale.format(this)
}

fun Number.numberFormatted(digitAfterComa: Int = 1, roundingMode: RoundingMode): String {
    decimalFormat.maximumFractionDigits = digitAfterComa
    decimalFormat.roundingMode = roundingMode
    return decimalFormat.format(this)
}

fun Number.productThousandFormatted(
    digitAfterComa: Int = 1,
    roundingMode: RoundingMode = RoundingMode.FLOOR,
    formatLimit : Int = 10000,
    isASGCDetailPage: Boolean = false
): String {
    if (toDouble() < formatLimit) return decimalThousandFormatted()

    val exp = (Math.log(this.toDouble()) / Math.log(1000.00)).toInt()
    val number = this.toDouble() / Math.pow(1000.00, exp.toDouble())
     return buildString {
        append(number.numberFormatted(digitAfterComa, roundingMode))
        if(isASGCDetailPage) append(" ")
        append(
            listOf("rb", "jt", "M", "T")[exp - 1]
        )
    }
}

fun Number.decimalThousandFormatted(): String =
    NumberFormat.getIntegerInstance(Locale("in", "id")).format(this)
