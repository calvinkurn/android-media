package com.tokopedia.imagepicker_insta.util

import java.util.concurrent.TimeUnit

object VideoUtil {
    const val DURATION_MAX_LIMIT = 59

    fun getFormattedDurationText(durationInMillis: Long): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % 60
        val secondText = if (seconds < 10) "0$seconds" else seconds
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis)
        val minuteText = if (minutes < 10) "0$minutes" else minutes
        return "$minuteText:$secondText"
    }

    fun isVideoWithinLimit(durationInMillis: Long): Boolean {
        return (durationInMillis / 1000) <= VideoUtil.DURATION_MAX_LIMIT
    }

}