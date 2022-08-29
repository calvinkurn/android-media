package com.tokopedia.kotlin.extensions.view

import java.util.Date
import kotlin.math.max
import kotlin.math.pow

/**
 * Created by jegul on 31/01/20
 */


const val THOUSAND_EXPONENT = 3


fun Long.toDate(): Date {
    return Date(this)
}


/**
 * Convert to non-nullable
 */
fun Long?.orZero(): Long = this ?: 0

/**
 * Using power as Long
 */
fun Long.pow(exponent: Int) = toDouble().pow(exponent).toLong()

/**
 * Convert long amount to string format
 * e.g. 1100 -> 1,1rb
 */
fun Long.toAmountString(
        ascendingSuffix: Array<String> = arrayOf("rb", "jt"),
        decimalPlaces: Int = 1,
        separator: String = ",",
        withSpacing: Boolean = false
): String {
    val exponent = THOUSAND_EXPONENT
    val multiplier: Long = 10L.pow(exponent)
    for ((index, suffix) in ascendingSuffix.withIndex()) {
        val nextPowerOfTen = 10.pow(index * exponent) * multiplier
        if (this >= nextPowerOfTen && this < nextPowerOfTen * multiplier) { return toAmountStringByDivider(nextPowerOfTen, suffix, decimalPlaces, separator, withSpacing) }
    }
    return this.toString()
}

/**
 * Convert amount to string format given specific denominator
 */
fun Long.toAmountStringByDivider(
        denominator: Long,
        suffix: String,
        decimalPlaces: Int,
        separator: String,
        withSpacing: Boolean
): String {
    val wholeNum = this/denominator
    val fractionNum = this%denominator
    return buildString {
        append(wholeNum)
        if (fractionNum > 0) {
            val fullString = this@toAmountStringByDivider.toString()
            val fractionString = fullString.substring(max(0, fullString.indexOf(wholeNum.toString()) + 1) + wholeNum.toString().length-1, fullString.length)
            append(separator)
            println(decimalPlaces)
            for (num in 0 until decimalPlaces) {
                if (num < fractionString.length) append("${fractionString[num]}")
            }
        }
        if (withSpacing) append(" ")
        append(suffix)
    }
}

fun Long?.isZero(): Boolean = this?.let { it == 0L } ?: false
fun Long?.isMoreThanZero(): Boolean = this?.let { it > 0L } ?: false
fun Long?.isLessThanZero(): Boolean = this?.let { it < 0L } ?: false
fun Long?.isOdd(): Boolean = this?.let { it % 2L == 1L } ?: false
fun Long?.isEven(): Boolean = this?.let { it % 2L == 0L } ?: false