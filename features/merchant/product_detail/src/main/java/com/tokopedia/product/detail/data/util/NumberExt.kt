package com.tokopedia.product.detail.data.util

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

fun Number.numberFormatted(): String {
    decimalFormat.maximumFractionDigits = 2
    return decimalFormat.format(this)
}

fun Number.thousandFormatted(): String {
    if (toDouble() < 1000) return numberFormatted()

    val exp = (Math.log(this.toDouble())/Math.log(1000.00)).toInt()
    val number = this.toDouble()/Math.pow(1000.00, exp.toDouble())
    return "${number.numberFormatted()}${listOf("rb", "jt", "M", "T")[exp-1]}"
}