package com.tokopedia.broadcaster.revamp.util.bitrate

import com.wmspanel.libstream.Streamer


/**
 * Created by meyta.taliti on 01/03/22.
 */
interface BroadcasterAdaptiveBitrate {

    fun setListener(listener: Listener)

    fun start(connectionId: Int)

    fun setFpsRanges(fpsRanges: List<Streamer.FpsRange>)

    fun resume()

    fun pause()

    fun stop()

    enum class Mode {
        LadderAscend, // Cut bitrate by 2/3 and increase it back to normal
        LogarithmicDescend // Gracefully descend from max bitrate by steps
    }

    class Builder(val mode: Mode) {
        var bitrate: Int = 0
            private set

        var fpsRanges: List<Streamer.FpsRange>? = null
            private set

        var delay = if (mode == Mode.LadderAscend) LA_DELAY else LD_DELAY
            private set

        var normalizationDelay = if (mode == Mode.LadderAscend) LA_NORMALIZATION_DELAY else LD_NORMALIZATION_DELAY
            private set

        var interval = if (mode == Mode.LadderAscend) LA_INTERVAL else LD_INTERVAL
            private set

        fun setFpsRanges(fpsRanges: List<Streamer.FpsRange>?) = apply {
            this.fpsRanges = fpsRanges
        }

        fun setBitrate(bitrate: Int) = apply { this.bitrate = bitrate }

        fun build() = BroadcasterAdaptiveBitrateImpl(this)

    }

    interface Listener {
        fun audioPacketsLost(connectionId: Int): Long?

        fun videoPacketsLost(connectionId: Int): Long?

        fun udpPacketsLost(connectionId: Int): Long?

        fun changeBitrate(bitrate: Int)

        fun changeFpsRange(fpsRange: Streamer.FpsRange)
    }

    companion object {
        private const val LA_DELAY = 2000
        private const val LA_NORMALIZATION_DELAY = 2000
        private const val LA_INTERVAL = 2000
        private const val LD_DELAY = 1000
        private const val LD_NORMALIZATION_DELAY = 1500
        private const val LD_INTERVAL = 500
    }
}