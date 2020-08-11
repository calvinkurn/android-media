package com.tokopedia.shop.common.util

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

val decimalFormat = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale("in", "id")))

fun Number.numberFormatted(digits: Int): String {
    decimalFormat.maximumFractionDigits = digits
    decimalFormat.roundingMode = RoundingMode.DOWN
    return decimalFormat.format(this)
}

fun Number.thousandFormatted(digits: Int = 2): String {
    if (toDouble() < 1000) return numberFormatted(digits)
    val exp = (Math.log(this.toDouble()) / Math.log(1000.00)).toInt()
    val number = this.toDouble() / Math.pow(1000.00, exp.toDouble())
    return "${number.numberFormatted(digits)}${listOf("rb", "jt", "M", "T")[exp - 1]}"
}