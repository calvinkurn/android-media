package com.tokopedia.play.broadcaster.pusher.statistic

/**
 * Created by meyta.taliti on 01/03/22.
 */
data class PlayBroadcasterMetric(
    val videoBitrate: Long,
    val audioBitrate: Long,
    val resolution: String,
    val traffic: Long,
    val bandwidth: Long,
    val fps: Double,
    val packetLossIncreased: Boolean,
    val videoBufferTimestamp: Long,
    val audioBufferTimestamp: Long,
    val authorId: String,
    val channelId: String,
)