package com.tokopedia.picker.common.mapper

import android.content.Context
import com.tokopedia.picker.common.R as commonPickerR

const val DEFAULT_DURATION_LABEL = "00:00"

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
     * Following exoplayer filde duration converter, added buffer 500ms
     * https://github.com/google/ExoPlayer/blob/release-v2/library/common/src/main/java/com/google/android/exoplayer2/util/Util.java#L1992
     */
    val duration = this + 500

    val second = duration / 1000 % 60
    val minute = duration / (1000 * 60) % 60
    val hour = duration / (1000 * 60 * 60) % 24

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