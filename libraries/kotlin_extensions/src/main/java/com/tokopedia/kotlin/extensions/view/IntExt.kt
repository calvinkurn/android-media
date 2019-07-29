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