package com.tokopedia.picker.common.mapper

import android.content.Context
import com.tokopedia.picker.common.R as commonPickerR

const val DEFAULT_DURATION_LABEL = "00:00"
const val ADDITIONAL_DURATION_BUFFER = 500
const val SECOND_TIME_DIVIDER = 1000
const val MINUTE_TIME_DIVIDER = SECOND_TIME_DIVIDER * 60
const val HOUR_TIME_DIVIDER = MINUTE_TIME_DIVIDER * 60
const val HOURS_ON_DAY = 24
const val MINUTES_ON_HOUR = 60
const val SECONDS_ON_MINUTE = 60

/**
 * Mapper of duration,
 *
 * @used:
 * Camera page
 *
 * @example:
 * -> 1654276461
 * -> 12:14:50
 */
fun Int?.humanize(): String {
    if (this == null) return DEFAULT_DURATION_LABEL

    /**
     * Following exoplayer duration converter, they added buffer time 500ms
     * https://github.com/google/ExoPlayer/blob/release-v2/library/common/src/main/java/com/google/android/exoplayer2/util/Util.java#L1992
     */
    val duration = this + ADDITIONAL_DURATION_BUFFER

    val second = duration / SECOND_TIME_DIVIDER % SECONDS_ON_MINUTE
    val minute = duration / MINUTE_TIME_DIVIDER % MINUTES_ON_HOUR
    val hour = duration / HOUR_TIME_DIVIDER % HOURS_ON_DAY

    return if (hour > 0) {
        String.format("%02d:%02d:%02d", hour, minute, second)
    } else {
        String.format("%02d:%02d", minute, second)
    }
}

/**
 * Map the video duration format with humanize
 *
 * @used:
 * validation toaster
 *
 * @xample:
 * -> 240924
 * -> 2 menit 3 detik
 */
fun Int.humanize(context: Context): String {
    val second = context.getString(commonPickerR.string.picker_video_duration_max_limit_sec)
    val minute = context.getString(commonPickerR.string.picker_video_duration_max_limit_min)

    return if (this < 60) {
        "$this $second"
    } else {
        val minResult = "${this / 60} $minute"
        val remainingSec = this % 60

        return if (remainingSec == 0) {
            minResult
        } else {
            "$minResult $remainingSec $second"
        }
    }
}