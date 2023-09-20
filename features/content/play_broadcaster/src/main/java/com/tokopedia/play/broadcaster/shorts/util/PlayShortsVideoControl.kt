package com.tokopedia.play.broadcaster.shorts.util

import android.util.Log
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.Renderer
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray

/**
 * Created by kenny.hadisaputra on 20/09/23
 */
class PlayShortsVideoControl(
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
        Log.d("PickerLoad", "Total AllocatedBytes: ${allocator.totalBytesAllocated}")
        Log.d("PickerLoad", "Buffered Duration (ms): ${bufferedDurationUs / 1000}")
        Log.d("PickerLoad", "Target Buffer Size: $mTargetBufferSize")
//        return loadControl.shouldContinueLoading(bufferedDurationUs, playbackSpeed)
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

    override fun onTracksSelected(
        renderers: Array<out Renderer>,
        trackGroups: TrackGroupArray,
        trackSelections: TrackSelectionArray
    ) {
        val targetBuffer = calculateTargetBufferSize(
            renderers,
            trackSelections
        )
        Log.d("PickerLoad", "Target Buffer: $targetBuffer")
        loadControl.onTracksSelected(renderers, trackGroups, trackSelections)
    }

    private fun onBufferingStuck() {
        loadingStuckCount++
        if (loadingStuckCount > 10) {
            mTargetBufferSize += 1_024 * 1_024
            loadingStuckCount = 0
        }
    }

    private fun calculateTargetBufferSize(
        renderers: Array<out Renderer>,
        trackSelectionArray: TrackSelectionArray
    ): Int {
        var targetBufferSize = 0
        for (i in renderers.indices) {
            if (trackSelectionArray[i] != null) {
                targetBufferSize += getDefaultBufferSize(renderers[i].trackType)
            }
        }
        return targetBufferSize
    }

    private fun getDefaultBufferSize(trackType: Int): Int {
        return when (trackType) {
            0 -> 36438016
            1 -> 3538944
            2 -> 32768000
            3 -> 131072
            4 -> 131072
            5 -> 131072
            6 -> 0
            else -> throw IllegalArgumentException()
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
