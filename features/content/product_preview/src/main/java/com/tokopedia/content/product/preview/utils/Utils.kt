package com.tokopedia.content.product.preview.utils

import android.content.Context
import com.tokopedia.content.product.preview.R
import java.util.concurrent.TimeUnit

@Suppress("MagicNumber")
fun Long.millisToFormattedVideoDuration(context: Context): String {
    val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(this)

    val secondsMod: Int
    val calculateSecond = totalSeconds % 60 + if (this % 1000 >= 500) 1 else 0
    val seconds = if (calculateSecond.toInt() == 60) {
        secondsMod = 1
        0
    } else {
        secondsMod = 0
        calculateSecond
    }

    val minutesMod: Int
    val calculateMinutes = secondsMod + TimeUnit.SECONDS.toMinutes(totalSeconds) % 60
    val minutes = if (calculateMinutes.toInt() == 60) {
        minutesMod = 1
        0
    } else {
        minutesMod = 0
        calculateMinutes
    }

    val hours = minutesMod + TimeUnit.SECONDS.toHours(totalSeconds)
    return if (hours <= 0) String.format(context.getString(R.string.video_time_less_than_hour), minutes, seconds)
    else String.format(context.getString(R.string.video_time_hours), hours, minutes, seconds)
}
