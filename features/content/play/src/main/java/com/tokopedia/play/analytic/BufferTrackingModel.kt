package com.tokopedia.play.analytic

/**
 * Created by jegul on 30/03/20
 */
data class BufferTrackingModel(
        val isBuffering: Boolean,
        val bufferCount: Int,
        val lastBufferMs: Long,
        val shouldTrackNext: Boolean
)