package com.tokopedia.broadcaster.revamp.util.statistic

/**
 * Created by meyta.taliti on 01/03/22.
 */
data class BroadcasterStatistic(
    val traffic: Long,
    val bandwidth: Long,
    val fps: Double,
    val packetLossIncreased: Boolean,
)