package com.tokopedia.picker.common.utils

import android.view.View

private const val BYTES_TO_MB = 1000000
private const val MILLIS_TO_SEC = 1000

fun Long.toMb(): Long {
    return this / BYTES_TO_MB
}

fun Int.toSec(): Int {
    return this / MILLIS_TO_SEC
}

fun Long.toSec(): Long {
    return this / MILLIS_TO_SEC
}

fun View.visibleWithCondition(isShown: Boolean) {
    visibility = if (isShown) View.VISIBLE else View.INVISIBLE
}