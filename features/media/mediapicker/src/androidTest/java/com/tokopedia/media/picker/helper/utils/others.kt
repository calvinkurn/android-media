package com.tokopedia.media.picker.helper.utils

import kotlin.math.ceil

/**
 * Converter for video duration to sec
 * ceil is used since the exoplayer did same strategies to ceiling the second
 * ex: real video duration is 4880 ms, then exoplayer timebar show it as 5s
 */
fun Long?.convertExoPlayerDuration(): Long {
    return ceil((this?.toFloat() ?: 0F) / 1000F).toLong()
}

/**
 * Convert int millis to sec
 */
fun Int.toSec(): Int {
    return this / 1000
}