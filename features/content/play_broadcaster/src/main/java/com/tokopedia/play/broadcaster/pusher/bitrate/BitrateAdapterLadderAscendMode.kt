package com.tokopedia.play.broadcaster.pusher.bitrate

import android.content.Context
import com.wmspanel.libstream.Streamer
import kotlin.math.roundToInt
import kotlin.math.roundToLong


/**
 * Created by mzennis on 17/06/21.
 */
class BitrateAdapterLadderAscendMode(context: Context) : BitrateAdapter(context) {

    companion object {

        private const val NORMALIZATION_DELAY: Long = 2000 // Ignore lost packets during this time after bitrate change
        private const val LOST_ESTIMATE_INTERVAL: Long = 10000 // Period for lost packets count
        private const val LOST_BANDWIDTH_TOLERANCE_FRACTION: Long = 300000
        private val BANDWIDTH_STEPS = doubleArrayOf(0.2, 0.25, 1.0 / 3.0, 0.450, 0.600, 0.780, 1.000)
        private val RECOVERY_ATTEMPT_INTERVALS = longArrayOf(15000, 60000, (60000 * 3).toLong())
        private val DROP_MERGE_INTERVAL = BANDWIDTH_STEPS.size * NORMALIZATION_DELAY * 2 // Period for bitrate drop duration
    }

    private var mStep = 0

    override fun start(streamer: Streamer, bitrate: Long, connectionId: Int) {
        mFullBitrate = bitrate
        mStep = 2
        val startBitrate = (bitrate * BANDWIDTH_STEPS[mStep]).roundToLong()
        super.start(streamer, startBitrate, connectionId)
        changeBitrateQuiet(startBitrate)
    }

    override fun check(audioLost: Long, videoLost: Long) {
        val curTime = System.currentTimeMillis()
        val prevLost = mLossHistory.lastElement()
        val prevBitrate = mBitrateHistory.lastElement()
        if (prevLost.audio != audioLost || prevLost.video != videoLost) {
            val dtChange = curTime - prevBitrate.ts
            mLossHistory.add(LossHistory(curTime, audioLost, videoLost))
            if (mStep == 0 || dtChange < NORMALIZATION_DELAY) return
            val estimatePeriod = (prevBitrate.ts + NORMALIZATION_DELAY).coerceAtLeast(curTime - LOST_ESTIMATE_INTERVAL)
            val lostTolerance = prevBitrate.bitrate / LOST_BANDWIDTH_TOLERANCE_FRACTION
            if (countLostForInterval(estimatePeriod) >= lostTolerance) {
                val newBitrate = (mFullBitrate * BANDWIDTH_STEPS[--mStep]).roundToLong()
                changeBitrate(newBitrate)
            }
        } else if (prevBitrate.bitrate < mFullBitrate && canTryToRecover()) {
            val newBitrate = (mFullBitrate * BANDWIDTH_STEPS[++mStep]).roundToLong()
            changeBitrate(newBitrate)
        }
    }

    private fun canTryToRecover(): Boolean {
        val curTime = System.currentTimeMillis()
        val len: Int = mBitrateHistory.size
        var numDrops = 0
        val numIntervals = RECOVERY_ATTEMPT_INTERVALS.size
        var prevDropTime: Long = 0
        for (i in len - 1 downTo 1) {
            val last = mBitrateHistory.elementAt(i)
            val prev = mBitrateHistory.elementAt(i - 1)
            val dt = curTime - last.ts
            if (last.bitrate < prev.bitrate) {
                if (prevDropTime != 0L && prevDropTime - last.ts < DROP_MERGE_INTERVAL) {
                    continue
                }
                if (dt <= RECOVERY_ATTEMPT_INTERVALS[numDrops]) {
                    return false
                }
                numDrops++
                prevDropTime = last.ts
            }
            if (numDrops == numIntervals || curTime - last.ts >= RECOVERY_ATTEMPT_INTERVALS[numIntervals - 1]) {
                break
            }
        }
        return true
    }

    override fun checkInterval(): Long = 2000

    override fun checkDelay(): Long = 2000
}