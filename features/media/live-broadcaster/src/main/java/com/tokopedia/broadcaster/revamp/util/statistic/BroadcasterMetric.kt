package com.tokopedia.broadcaster.revamp.util.statistic

/**
 * Created by meyta.taliti on 01/03/22.
 */
data class BroadcasterMetric(
    val videoBitrate: Long,
    val audioBitrate: Long,
    val resolutionWidth: Long,
    val resolutionHeight: Long,
    val traffic: Long,
    val bandwidth: Long,
    val fps: Double,
    val packetLossIncreased: Boolean,
    val videoBufferTimestamp: Long,
    val audioBufferTimestamp: Long,
) {
    companion object {
        val Empty: BroadcasterMetric
            get() = BroadcasterMetric(
                videoBitrate = 0,
                audioBitrate = 0,
                resolutionWidth = 0,
                resolutionHeight = 0,
                traffic = 0,
                bandwidth = 0,
                fps = 0.0,
                packetLossIncreased = false,
                videoBufferTimestamp = 0,
                audioBufferTimestamp = 0,
            )
    }
}