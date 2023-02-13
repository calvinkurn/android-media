package com.tokopedia.hotel.search_map.data.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

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

    val exp = (ln(this.toDouble()) / ln(1000.00)).toInt()
    val number = this.toDouble()/ 1000.00.pow(exp.toDouble())
    return "${number.numberFormatted()}${listOf("rb", "jt", "M", "T")[exp-1]}"
}