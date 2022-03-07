package com.tokopedia.broadcaster.bitrate

import com.tokopedia.broadcaster.lib.LarixStreamer
import kotlin.math.max
import kotlin.math.min

class LogarithmicDescendMode : BitrateAdapter() {

    var mMinBitrate =  0L

    override fun start(streamer: LarixStreamer, bitrate: Long, connectionId: Int) {
        mFullBitrate = bitrate
        mMinBitrate = bitrate / LOST_TOLERANCE
        super.start(streamer, bitrate, connectionId)
    }

    override fun check(audioLost: Long, videoLost: Long) {
        val curTime = System.currentTimeMillis()
        val prevLost = mLossHistory.lastElement()
        val prevBitrate = mBitrateHistory.lastElement()
        val lastChange = max(prevBitrate.ts, prevLost.ts)
        if (prevLost.audio != audioLost || prevLost.video != videoLost) {
            val dtChange = curTime - prevBitrate.ts
            mLossHistory.add(LossHistory(curTime, audioLost, videoLost))
            if (prevBitrate.bitrate <= mMinBitrate || dtChange < NORMALIZATION_DELAY) return
            val estimatePeriod = max(prevBitrate.ts + NORMALIZATION_DELAY, curTime - LOST_ESTIMATE_INTERVAL)
            if (countLostForInterval(estimatePeriod) >= LOST_TOLERANCE) {
                val newBitrate = max(mMinBitrate, prevBitrate.bitrate * 1000 / 1414)
                changeBitrate(newBitrate)
            }
        } else if (prevBitrate.bitrate != mBitrateHistory.firstElement().bitrate
            && curTime - lastChange >= RECOVERY_ATTEMPT_INTERNAL) {
            val newBitrate = min(mFullBitrate, prevBitrate.bitrate * 1415 / 1000)
            changeBitrate(newBitrate)
        }
    }

    override fun checkInterval(): Long = 1000

    override fun checkDelay(): Long = 1000

    companion object {
        // Ignore lost packets during this time after bitrate change
        const val NORMALIZATION_DELAY: Long = 1500

        // Period for lost packets count
        private const val LOST_ESTIMATE_INTERVAL: Long = 10000

        // Period for lost packets count
        private const val LOST_TOLERANCE = 4

        private const val RECOVERY_ATTEMPT_INTERNAL = 60000
    }

}