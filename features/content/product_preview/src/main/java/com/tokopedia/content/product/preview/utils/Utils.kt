package com.tokopedia.content.product.preview.utils

import android.content.Context
import com.tokopedia.content.product.preview.R
import java.util.concurrent.TimeUnit

fun Long.millisToFormattedVideoDuration(context: Context): String {
    val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(this)
    val seconds = totalSeconds % 60 + if (this % 1000 >= 500) 1 else 0
    val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60
    val hours = TimeUnit.SECONDS.toHours(totalSeconds)
    return if (hours <= 0) String.format(context.getString(R.string.video_time_less_than_hour), minutes, seconds)
    else String.format(context.getString(R.string.video_time_hours), hours, minutes, seconds)
}
