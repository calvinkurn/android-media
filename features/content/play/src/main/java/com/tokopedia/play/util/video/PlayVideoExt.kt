package com.tokopedia.play.util.video

import java.util.concurrent.TimeUnit

/**
 * Created by kenny.hadisaputra on 31/03/22
 */
fun Long.millisToFormattedVideoDuration(): String {
    val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(this)
    val seconds = totalSeconds % 60 + if (this % 1000 >= 500) 1 else 0
    val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60
    val hours = TimeUnit.SECONDS.toHours(totalSeconds)
    return if (hours <= 0) String.format("%d:%02d", minutes, seconds)
    else String.format("%d:%02d:%02d", hours, minutes, seconds)
}