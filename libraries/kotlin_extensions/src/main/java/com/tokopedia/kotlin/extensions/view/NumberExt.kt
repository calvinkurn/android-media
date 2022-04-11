package com.tokopedia.kotlin.extensions.view

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

fun Number.numberFormatted(maximumFractionDigits: Int, roundingMode: RoundingMode): String {
    decimalFormat.maximumFractionDigits = maximumFractionDigits
    decimalFormat.roundingMode = roundingMode
    return decimalFormat.format(this)
}

fun Number.thousandFormatted(
        digit: Int = 2,
        roundingMode: RoundingMode = RoundingMode.HALF_EVEN,
        hasSpace: Boolean = false
): String {
    if (toDouble() < 1000) return numberFormatted(digit, roundingMode)

    val exp = (Math.log(this.toDouble()) / Math.log(1000.00)).toInt()
    val number = this.toDouble() / Math.pow(1000.00, exp.toDouble())

    if (hasSpace) {
        return "${number.numberFormatted(digit, roundingMode)}${
            listOf(
                " rb",
                " jt",
                " M",
                " T"
            )[exp - 1]
        }"
    } else {
        return "${number.numberFormatted(digit, roundingMode)}${
            listOf(
                "rb",
                "jt",
                "M",
                "T"
            )[exp - 1]
        }"
    }
}

fun Number.getNumberFormatted(): String {
    val format = DecimalFormat("###,###").format(this)
    return format.replace(",", ".")
}