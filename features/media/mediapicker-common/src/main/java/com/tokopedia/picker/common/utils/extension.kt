package com.tokopedia.picker.common.utils

import android.content.Context
import com.tokopedia.picker.common.R.string as stringRes

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

fun Long.toReadableTime(context: Context): String{
    return if(this < 60){
        "$this ${context.resources.getString(stringRes.picker_video_duration_max_limit_sec)}"
    } else {
        "${this / 60} ${context.resources.getString(stringRes.picker_video_duration_max_limit_min)}"
    }
}