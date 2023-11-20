package com.tokopedia.media.preview.ui.player

import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.LoadControl

/**
 * Created by kenny.hadisaputra on 20/09/23
 */
class PickerVideoLoadControl(
    private val loadControl: LoadControl = DefaultLoadControl.Builder()
        .setPrioritizeTimeOverSizeThresholds(PRIORITIZE_TIME_OVER_SIZE)
        .setBufferDurationsMs(
            MIN_BUFFER_US,
            MAX_BUFFER_US,
            BUFFER_FOR_PLAYBACK_US,
            BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_US
        )
        .setTargetBufferBytes(TARGET_BUFFER_SIZE)
        .createDefaultLoadControl()
) : LoadControl by loadControl {

    private var loadingStuckCount = 0

    private var mBuffering = false

    private var mTargetBufferSize = TARGET_BUFFER_SIZE

    override fun onPrepared() {
        loadControl.onPrepared()
        mBuffering = false
    }

    override fun onStopped() {
        loadControl.onStopped()
        mBuffering = false
    }

    override fun onReleased() {
        loadControl.onReleased()
        mBuffering = false
    }

    override fun shouldContinueLoading(bufferedDurationUs: Long, playbackSpeed: Float): Boolean {
        val targetBufferSizeReached = this.allocator.totalBytesAllocated >= mTargetBufferSize
        if (bufferedDurationUs < MIN_BUFFER_US) {
            val prevBuffering = mBuffering
            mBuffering = PRIORITIZE_TIME_OVER_SIZE || !targetBufferSizeReached

            if (!prevBuffering && !mBuffering) onBufferingStuck()
        } else if (bufferedDurationUs >= MAX_BUFFER_US || targetBufferSizeReached) {
            mBuffering = false
        }

        return mBuffering
    }

    private fun onBufferingStuck() {
        loadingStuckCount++
        if (loadingStuckCount > 10) {
            mTargetBufferSize += 1_024 * 1_024
            loadingStuckCount = 0
        }
    }

    companion object {
        private const val MIN_BUFFER_US = 1_000 * 1_000
        private const val MAX_BUFFER_US = 3_000 * 1_000
        private const val BUFFER_FOR_PLAYBACK_US = 1_000 * 1_000
        private const val BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_US = 1_000 * 1_000
        private const val TARGET_BUFFER_SIZE = 3 * 1_024 * 1_024
        private const val PRIORITIZE_TIME_OVER_SIZE = false
    }
}
