package com.tokopedia.broadcaster.revamp.util.statistic

import com.tokopedia.kotlin.extensions.view.orZero

/**
 * Created by meyta.taliti on 01/03/22.
 */
class BroadcasterStatisticManager(
    private val listener: Listener,
) {

    private var prevTime: Long = 0
    private var prevBytes: Long = 0
    private var bps: Long = 0
    private var fps = 0.0
    private var packetLossIncreased = false

    private var videoPacketsLost: Long = 0
    private var audioPacketsLost: Long = 0
    private var udpPacketsLost: Long = 0

    fun start(connectionId: Int) {
        prevTime = System.currentTimeMillis()
        prevBytes = listener.bytesSent(connectionId).orZero()
    }

    fun update(connectionId: Int) {
        if (!listener.isEligibleQuery()) return

        val curTime = System.currentTimeMillis()

        val bytesSent = listener.bytesSent(connectionId).orZero()
        val timeDiff = curTime - prevTime

        bps = if (timeDiff > 0) 8 * 1000 * (bytesSent - prevBytes) / timeDiff else 0
        prevTime = curTime
        prevBytes = bytesSent
        fps = listener.fps().orZero()

        packetLossIncreased = false

        val audioLost: Long = listener.audioPacketsLost(connectionId) ?: return
        val videoLost: Long = listener.videoPacketsLost(connectionId) ?: return
        val udpLost: Long = listener.udpPacketsLost(connectionId) ?: return

        if (audioPacketsLost != audioLost || videoPacketsLost != videoLost || udpPacketsLost != udpLost) {
            audioPacketsLost = udpLost
            videoPacketsLost = udpLost
            udpPacketsLost = udpLost
            packetLossIncreased = true
        }
    }

    fun getStatistic() = BroadcasterStatistic(
        traffic = prevBytes,
        bandwidth = bps,
        fps = fps,
        packetLossIncreased = packetLossIncreased
    )

    interface Listener {
        // some auth schemes require reconnection to same url multiple times
        // app should not query connection statistics while auth phase is in progress
        fun isEligibleQuery(): Boolean

        fun fps(): Double?

        fun bytesSent(connectionId: Int): Long?

        fun audioPacketsLost(connectionId: Int): Long?

        fun videoPacketsLost(connectionId: Int): Long?

        fun udpPacketsLost(connectionId: Int): Long?
    }
}