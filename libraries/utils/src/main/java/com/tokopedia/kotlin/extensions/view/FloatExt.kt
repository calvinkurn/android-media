package com.tokopedia.kotlin.extensions.view

import android.content.res.Resources

fun Float?.orZero(): Float = this ?: 0f

fun Float?.toIntSafely(): Int {
    return try {
        orZero().toInt()
    } catch (e: Exception) {
        0
    }
}

fun Float.toDp(): Float = this / Resources.getSystem().displayMetrics.density

fun Float.toPx(): Float = this * Resources.getSystem().displayMetrics.density

val Float.Companion.ZERO get() = 0f