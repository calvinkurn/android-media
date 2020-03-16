package com.tokopedia.play_common.player

import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.LoadControl
import com.tokopedia.play_common.model.PlayBufferControl

/**
 * Created by jegul on 15/03/20
 */
class PlayVideoLoadControl private constructor(
        private val loadControl: DefaultLoadControl
) : LoadControl by loadControl {

    val bufferControl: PlayBufferControl
        get() {
            return if (::_bufferControl.isInitialized) _bufferControl
            else throw IllegalAccessException("Please use PlayVideoLoadControl(bufferControl: PlayBufferControl) constructor")
        }

    private lateinit var _bufferControl: PlayBufferControl

    constructor(bufferControl: PlayBufferControl) : this(
            DefaultLoadControl.Builder()
                    .setBufferDurationsMs(
                            bufferControl.minBufferMs,
                            bufferControl.maxBufferMs,
                            bufferControl.bufferForPlaybackMs,
                            bufferControl.bufferForPlaybackAfterRebufferMs
                    ).createDefaultLoadControl()
    ) {
        _bufferControl = bufferControl
    }

    private var preventLoading: Boolean = false

    override fun shouldContinueLoading(bufferedDurationUs: Long, playbackSpeed: Float): Boolean {
        return if (preventLoading) false
        else loadControl.shouldContinueLoading(bufferedDurationUs, playbackSpeed)
    }

    fun setPreventLoading(shouldPrevent: Boolean) {
        preventLoading = shouldPrevent
    }
}