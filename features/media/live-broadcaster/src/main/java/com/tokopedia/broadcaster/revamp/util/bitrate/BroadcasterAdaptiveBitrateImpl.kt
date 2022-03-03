package com.tokopedia.broadcaster.revamp.util.bitrate

import com.wmspanel.libstream.Streamer.FpsRange
import java.util.*
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * Created by meyta.taliti on 01/03/22.
 */
class BroadcasterAdaptiveBitrateImpl(
    builder: BroadcasterAdaptiveBitrate.Builder,
) : BroadcasterAdaptiveBitrate {

    private var mListener: BroadcasterAdaptiveBitrate.Listener? = null

    private val mLossHistory: Vector<LossHistory> = Vector<LossHistory>()
    private val mBitrateHistory: Vector<BitrateHistory> = Vector<BitrateHistory>()

    private var mFullBitrate = 0
    private var mCurrentBitrate = 0
    private var mCurrentFps = 0.0
    private var mMaxFps = 30.0

    private val mMode = builder.mode
    private val mSettingsBitrate = builder.bitrate
    private val mDelay = builder.delay
    private val mInterval = builder.interval
    private val mNormalizationDelay = builder.normalizationDelay

    private var mCurrentRange = FpsRange(30, 30)
    private var mFpsRanges: List<FpsRange>? = builder.fpsRanges

    private var mConnectionId: Int? = null
    private var mCheckTimer: Timer? = null

    private var mStep = 0
    private var mMinBitrate = 0

    private val bandwidthSteps = doubleArrayOf(0.2, 0.25, 1.0 / 3.0, 0.450, 0.600, 0.780, 1.000)
    private val recoveryAttemptIntervals = longArrayOf(15000, 60000, (60000 * 3).toLong())
    private val dropMergeInterval: Int = bandwidthSteps.size * mNormalizationDelay * 2 // period for bitrate drop duration

    override fun setListener(listener: BroadcasterAdaptiveBitrate.Listener) {
        this.mListener = listener
    }

    override fun start(connectionId: Int) {
        mFullBitrate = mSettingsBitrate
        mMinBitrate = mSettingsBitrate / 4
        mStep = 2

        val startBitrate = (mSettingsBitrate * bandwidthSteps.get(mStep)).roundToInt()
        mCurrentBitrate = if (mMode === BroadcasterAdaptiveBitrate.Mode.LadderAscend) startBitrate else mSettingsBitrate

        mConnectionId = connectionId
        mLossHistory.clear()
        mBitrateHistory.clear()

        val currentTimeMillis = System.currentTimeMillis()
        mLossHistory.add(LossHistory(currentTimeMillis, 0, 0))
        mBitrateHistory.add(BitrateHistory(currentTimeMillis, mCurrentBitrate.toLong()))

        mCurrentFps = mMaxFps

        runTask()

        if (mMode === BroadcasterAdaptiveBitrate.Mode.LadderAscend) changeBitrateQuietly(startBitrate.toLong())
    }

    override fun setFpsRanges(fpsRanges: List<FpsRange>) {
        mFpsRanges = fpsRanges
    }

    override fun resume() {
        if (mCurrentBitrate == 0) return
        
        mCurrentBitrate = mFullBitrate
        mMaxFps = 30.0
        mCurrentRange = FpsRange(30, 30)
        mCurrentFps = mMaxFps
        runTask()
        mListener?.changeBitrate(mFullBitrate)
    }

    override fun pause() {
        mCheckTimer?.cancel()
    }

    override fun stop() {
        if (mFullBitrate > 0) updateFps(mFullBitrate.toLong())
        mCurrentBitrate = 0
        mListener = null
        mConnectionId = null
        mCheckTimer?.cancel()
    }

    private fun runTask() {
        if (mDelay == 0 || mInterval == 0) return
        val checkNetwork: TimerTask = object : TimerTask() {
            override fun run() {
                val listener = mListener ?: return
                val connectionId = mConnectionId ?: return

                val audioLost = listener.audioPacketsLost(connectionId)
                var videoLost = listener.videoPacketsLost(connectionId)
                val udpLost = listener.udpPacketsLost(connectionId)

                if (audioLost != null && videoLost != null && udpLost != null) {
                    videoLost += udpLost
                    check(audioLost, videoLost)
                }
            }
        }
        mCheckTimer = Timer()
        mCheckTimer?.schedule(checkNetwork, mDelay.toLong(), mInterval.toLong())
    }

    private fun check(audioLost: Long, videoLost: Long) {
        when (mMode) {
            BroadcasterAdaptiveBitrate.Mode.LogarithmicDescend -> checkLogarithmicDescend(audioLost, videoLost)
            BroadcasterAdaptiveBitrate.Mode.LadderAscend -> checkLadderAscend(audioLost, videoLost)
        }
    }

    private fun checkLadderAscend(audioLost: Long, videoLost: Long) {
        val curTime = System.currentTimeMillis()
        val prevLost = mLossHistory.lastElement()
        val prevBitrate = mBitrateHistory.lastElement()
        if (prevLost.audio != audioLost || prevLost.video != videoLost) {
            val dtChange: Long = curTime - prevBitrate.ts
            mLossHistory.add(LossHistory(curTime, audioLost, videoLost))
            if (mStep == 0 || dtChange < mNormalizationDelay) return

            val estimatePeriod: Long =
                (prevBitrate.ts + mNormalizationDelay).coerceAtLeast(curTime - LOST_ESTIMATE_INTERVAL)
            val lostTolerance: Long =
                prevBitrate.bitrate / LOST_BANDWIDTH_TOLERANCE_FRACTION
            if (countLostForInterval(estimatePeriod) >= lostTolerance) {
                val newBitrate = (mFullBitrate * bandwidthSteps[--mStep]).roundToInt()
                changeBitrate(newBitrate.toLong())
            }
        } else if (prevBitrate.bitrate < mFullBitrate && canTryToRecover()) {
            val newBitrate = (mFullBitrate * bandwidthSteps.get(++mStep)).roundToInt()
            changeBitrate(newBitrate.toLong())
        }
    }

    private fun checkLogarithmicDescend(audioLost: Long, videoLost: Long) {
        val curTime = System.currentTimeMillis()
        val prevLost: LossHistory = mLossHistory.lastElement()
        val prevBitrate: BitrateHistory = mBitrateHistory.lastElement()
        val lastChange: Long = prevBitrate.ts.coerceAtLeast(prevLost.ts)
        if (prevLost.audio != audioLost || prevLost.video != videoLost) {
            val dtChange: Long = curTime - prevBitrate.ts
            mLossHistory.add(LossHistory(curTime, audioLost, videoLost))
            if (prevBitrate.bitrate <= mMinBitrate || dtChange < mNormalizationDelay) {
                return
            }
            val estimatePeriod: Long =
                (prevBitrate.ts + mNormalizationDelay).coerceAtLeast(curTime - LOST_ESTIMATE_INTERVAL)
            if (countLostForInterval(estimatePeriod) >= LOST_TOLERANCE) {
                val newBitrate =
                    mMinBitrate.toLong().coerceAtLeast(prevBitrate.bitrate * 1000 / 1414)
                changeBitrate(newBitrate)
            }
        } else if (prevBitrate.bitrate != mBitrateHistory.firstElement().bitrate &&
            curTime - lastChange >= RECOVERY_ATTEMPT_INTERVAL
        ) {
            val newBitrate = mFullBitrate.toLong().coerceAtMost(prevBitrate.bitrate * 1415 / 1000)
            changeBitrate(newBitrate)
        }
    }

    private fun changeBitrate(newBitrate: Long) {
        val curTime = System.currentTimeMillis()
        mBitrateHistory.add(BitrateHistory(curTime, newBitrate))
        updateFps(newBitrate)
        mListener?.changeBitrate(newBitrate.toInt())
        mCurrentBitrate = newBitrate.toInt()
    }

    private fun changeBitrateQuietly(newBitrate: Long) {
        mListener?.changeBitrate(newBitrate.toInt())
    }

    private fun countLostForInterval(interval: Long): Long {
        var lostPackets: Long = 0
        val last: LossHistory = mLossHistory.lastElement()
        for (i in mLossHistory.indices.reversed()) {
            if (mLossHistory.elementAt(i).ts < interval) {
                val h: LossHistory = mLossHistory.elementAt(i)
                lostPackets = last.video - h.video + (last.audio - h.audio)
                break
            }
        }
        return lostPackets
    }

    private fun canTryToRecover(): Boolean {
        val curTime = System.currentTimeMillis()
        val len = mBitrateHistory.size
        var numDrops = 0
        val numIntervals: Int = recoveryAttemptIntervals.size
        var prevDropTime: Long = 0
        for (i in len - 1 downTo 1) {
            val last: BitrateHistory =
                mBitrateHistory.elementAt(i)
            val prev: BitrateHistory =
                mBitrateHistory.elementAt(i - 1)
            val dt: Long = curTime - last.ts
            if (last.bitrate < prev.bitrate) {
                if (prevDropTime != 0L && prevDropTime - last.ts < dropMergeInterval) {
                    continue
                }
                if (dt <= recoveryAttemptIntervals.get(numDrops)) {
                    return false
                }
                numDrops++
                prevDropTime = last.ts
            }
            if (numDrops == numIntervals || curTime - last.ts >= recoveryAttemptIntervals.get(
                    numIntervals - 1)
            ) {
                break
            }
        }
        return true
    }

    private fun updateFps(newBitrate: Long) {
        val fpsRanges = mFpsRanges ?: return

        val bitrateRel = newBitrate * 1.0 / mFullBitrate
        var relFps = mMaxFps
        if (bitrateRel < 0.5) {
            relFps = 15.0.coerceAtLeast(floor(mMaxFps * bitrateRel * 2.0 / 5.0) * 5.0)
        }
        if (abs(relFps - mCurrentFps) < 1.0) {
            return
        }
        mCurrentFps = relFps
        val newRange = nearestFpsRange(fpsRanges, relFps.roundToInt().toFloat(), false)
        if (newRange.fpsMax == mCurrentRange.fpsMax && newRange.fpsMin == mCurrentRange.fpsMin) {
            return
        }
        mListener?.changeFpsRange(newRange)
        mCurrentRange = newRange
    }

    // Find best matching FPS range
    // (fpsMax is much important to be closer to target, so we squared it)
    // In strict mode targetFps will be exact within range, otherwise just as close as possible
    private fun nearestFpsRange(
        fpsRanges: List<FpsRange>,
        targetFps: Float,
        strict: Boolean,
    ): FpsRange {
        //Find best matching FPS range
        // (fpsMax is much important to be closer to target, so we squared it)
        var minDistance = 1e10f
        var range = FpsRange(0, 0)
        for (r in fpsRanges) {
            if (strict && (r.fpsMin > targetFps || r.fpsMax < targetFps)) {
                continue
            }
            val distance =
                (r.fpsMax - targetFps) * (r.fpsMax - targetFps) + abs(r.fpsMin - targetFps)
            if (distance < minDistance) {
                range = r
                if (distance < 0.01f) {
                    break
                }
                minDistance = distance
            }
        }
        return range
    }

    internal class LossHistory(val ts: Long, val audio: Long, val video: Long)

    internal class BitrateHistory(val ts: Long, val bitrate: Long)

    companion object {
        private const val LOST_TOLERANCE = 4 // maximum acceptable number of lost packets
        private const val RECOVERY_ATTEMPT_INTERVAL = 60000
        private const val LOST_ESTIMATE_INTERVAL = 10000 // period for lost packets count
        private const val LOST_BANDWIDTH_TOLERANCE_FRACTION = 300000
    }
}