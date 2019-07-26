package com.tokopedia.kotlin.extensions.view

import android.util.DisplayMetrics

/**
 * @author : Steven 05/07/19
 */

fun Int.dpToPx(displayMetrics: DisplayMetrics): Int = (this * displayMetrics.density).toInt()

fun Int.pxToDp(displayMetrics: DisplayMetrics): Int = (this / displayMetrics.density).toInt()
