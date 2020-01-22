package com.tokopedia.play.util

import kotlin.math.pow

/**
 * Created by jegul on 16/01/20
 */
fun Int.pow(exponent: Int) = toDouble().pow(exponent).toInt()
fun Long.pow(exponent: Int) = toDouble().pow(exponent).toLong()

fun Long.toCompactAmountString(
        ascendingSuffix: Array<String> = arrayOf("rb", "jt"),
        decimalPlaces: Int = 1,
        divider: String = ""
): String {
    val exponent = 3
    val multiplier: Long = 10L.pow(exponent)
    for ((index, suffix) in ascendingSuffix.withIndex()) {
        val nextPowerOfTen = 10.pow(index * exponent) * multiplier
        if (this >= nextPowerOfTen && this < nextPowerOfTen * multiplier) { return toCompactAmountStringByDivider(nextPowerOfTen, suffix, decimalPlaces, divider) }
    }
    return this.toString()
}

fun Long.toCompactAmountStringByDivider(
        denominator: Long,
        suffix: String,
        decimalPlaces: Int = 1,
        divider: String = ""
): String {
    val wholeNum = this/denominator
    val fractionNum = this%denominator
    return buildString {
        append(wholeNum)
        if (fractionNum > 0) {
            val fractionString = fractionNum.toString()
            append(",")
            for (num in 0 until decimalPlaces) {
                if (num < fractionString.length) append("${fractionNum.toString()[num]}")
            }
        }
        append(divider)
        append(suffix)
    }
}

fun Int.toCompactAmountString(
        ascendingSuffix: Array<String> = arrayOf("rb", "jt"),
        decimalPlaces: Int = 1,
        divider: String = ""
): String = toLong().toCompactAmountString()
