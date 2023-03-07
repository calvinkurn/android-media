package com.tokopedia.reviewcommon.extension

import android.graphics.Color

fun String?.toColorInt(defaultColor: Int): Int {
    return try {
        Color.parseColor(this)
    } catch (t: Throwable) {
        defaultColor
    }
}