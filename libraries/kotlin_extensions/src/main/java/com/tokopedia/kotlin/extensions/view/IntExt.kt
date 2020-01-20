package com.tokopedia.kotlin.extensions.view
import android.util.DisplayMetrics
/**
 * @author : Steven 05/07/19
 */

/**
 * Get value in range of min max
 */
fun Int.clamp(min: Int, max: Int): Int {
    if (this < min) return min
    return if (this > max) max else this
}

fun Int.dpToPx(displayMetrics: DisplayMetrics): Int = (this * displayMetrics.density).toInt()

fun Int.pxToDp(displayMetrics: DisplayMetrics): Int = (this / displayMetrics.density).toInt()


fun Int?.toZeroIfNull():Int {
    return this?:0
}


const val INTEGER_MILLION = 1_000_000
const val INTEGER_THOUSAND = 1_000

fun Int.toCompactAmountString(): String {
    return when {
        this >= INTEGER_MILLION -> toCompactAmountStringByDivider(INTEGER_MILLION, "jt")
        this >= INTEGER_THOUSAND -> toCompactAmountStringByDivider(INTEGER_THOUSAND, "rb")
        else -> toString()
    }
}

fun Int.toCompactAmountStringByDivider(divider: Int, suffix: String): String {
    val integerNum = this/divider
    val nonIntegerNum = this%divider
    return buildString {
        append(integerNum)
        if (nonIntegerNum > 0) append(",${nonIntegerNum.toString()[0]}")
        append(suffix)
    }
}

fun Int?.orZero(): Int = this ?: 0

fun Long?.orZero(): Long = this ?: 0

fun Float?.orZero(): Float = this ?: 0f

fun Double?.orZero(): Double = this ?: 0f.toDouble()