package com.tokopedia.play.widget.player

import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.source.TrackGroup
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.upstream.BandwidthMeter

/**
 * @author by astidhiyaa on 13/12/22
 */
class CustomTrackSelection constructor(
    group: TrackGroup,
    tracks: IntArray,
    bandwidthMeter: BandwidthMeter,
    private val limitBitrate: BitrateLimit,
) : AdaptiveTrackSelection(group, tracks, bandwidthMeter) {

    class Factory constructor(
        private val limitBitrate: BitrateLimit
    ) : AdaptiveTrackSelection.Factory() {

        override fun createAdaptiveTrackSelection(
            group: TrackGroup,
            bandwidthMeter: BandwidthMeter,
            tracks: IntArray,
            totalFixedTrackBandwidth: Int
        ): AdaptiveTrackSelection {
            return CustomTrackSelection(
                group,
                tracks,
                bandwidthMeter,
                limitBitrate
            )
        }
    }

    override fun canSelectFormat(
        format: Format,
        trackBitrate: Int,
        playbackSpeed: Float,
        effectiveBitrate: Long
    ): Boolean {
        val limitedEffectiveBitrate = limitBitrate.getMaxBitrate().coerceAtMost(effectiveBitrate)
        return super.canSelectFormat(format, trackBitrate, playbackSpeed, limitedEffectiveBitrate)
    }
}

class BitrateLimit {
    private var maxBitrate = Long.MAX_VALUE

    fun setMaxBitrate(bitrate: Long) {
        this.maxBitrate = bitrate
    }

    fun getMaxBitrate(): Long {
        return maxBitrate
    }
}
