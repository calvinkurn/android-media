package com.tokopedia.play_common.model

import com.google.android.exoplayer2.DefaultLoadControl

/**
 * Created by jegul on 12/03/20
 */
data class PlayBufferControl(
        val minBufferMs: Int = DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
        val maxBufferMs: Int = DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
        val bufferForPlaybackMs: Int = DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
        val bufferForPlaybackAfterRebufferMs: Int = DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
)