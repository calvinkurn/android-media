package com.tokopedia.kotlin.extensions.view

/**
 * Created by jegul on 31/01/20
 */

/**
 * Convert long amount to string format
 * e.g. 1100 -> 1,1rb
 */
fun Long.toAmountString(
        ascendingSuffix: Array<String> = arrayOf("rb", "jt"),
        decimalPlaces: Int = 1,
        divider: String = ""
): String {
    val exponent = 3
    val multiplier: Long = 10L.pow(exponent)
    for ((index, suffix) in ascendingSuffix.withIndex()) {
        val nextPowerOfTen = 10.pow(index * exponent) * multiplier
        if (this >= nextPowerOfTen && this < nextPowerOfTen * multiplier) { return toAmountStringByDivider(nextPowerOfTen, suffix, decimalPlaces, divider) }
    }
    return this.toString()
}

/**
 * Convert amount to string format given specific denominator
 */
fun Long.toAmountStringByDivider(
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