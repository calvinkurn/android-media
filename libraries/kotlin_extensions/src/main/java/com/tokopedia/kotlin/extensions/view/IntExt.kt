package com.tokopedia.kotlin.extensions.view
import android.util.DisplayMetrics
import kotlin.math.pow

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

fun Int?.isZero(): Boolean = this?.let { it == 0 } ?: false
fun Int?.isMoreThanZero(): Boolean = this?.let { it > 0 } ?: false
fun Int?.isLessThanZero(): Boolean = this?.let { it < 0 } ?: false
fun Int?.isOdd(): Boolean = this?.let { it % 2 == 1 } ?: false
fun Int?.isEven(): Boolean = this?.let { it % 2 == 0 } ?: false

const val INTEGER_MILLION = 1_000_000
const val INTEGER_THOUSAND = 1_000
/**
 * Using power as Int
 */
fun Int.pow(exponent: Int) = toDouble().pow(exponent).toInt()

/**
 * Convert int amount to string format
 * e.g. 1100 -> 1,1rb
 */
fun Int.toAmountString(
        ascendingSuffix: Array<String> = arrayOf("rb", "jt"),
        decimalPlaces: Int = 1,
        separator: String = ",",
        withSpacing: Boolean = false
): String = toLong().toAmountString(ascendingSuffix, decimalPlaces, separator, withSpacing)

fun Int?.orZero(): Int = this ?: 0