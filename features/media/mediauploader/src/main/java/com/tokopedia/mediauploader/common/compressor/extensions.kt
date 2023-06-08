package com.tokopedia.mediauploader.common.compressor

import android.media.MediaCodec
import android.media.MediaCodec.BufferInfo

fun MediaCodec.safeQueueInputBuffer(
    bufferIndex: Int,
    offset: Int = 0,
    size: Int = 0,
    presentationTime: Long = 0L,
    flags: Int = 0
) {
    queueInputBuffer(
        bufferIndex,
        offset,
        size,
        presentationTime,
        flags
    )
}

fun BufferInfo.atLeastNotEmpty(): Boolean {
    return size > 1
}

fun BufferInfo.isNotZero(): Boolean {
    return size != 0
}

fun BufferInfo.presentationTimeAsSec(): Long {
    return presentationTimeUs * 1000
}
