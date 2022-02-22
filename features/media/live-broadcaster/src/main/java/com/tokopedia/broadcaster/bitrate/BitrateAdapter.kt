package com.tokopedia.broadcaster.bitrate

import com.tokopedia.broadcaster.lib.LarixStreamer
import com.tokopedia.broadcaster.data.BitrateMode
import com.wmspanel.libstream.Streamer.FpsRange
import java.util.*
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToLong

abstract class BitrateAdapter {

    var mFullBitrate: Long = 0
    var mLossHistory: Vector<LossHistory> = Vector()
    var mBitrateHistory: Vector<BitrateHistory> = Vector()

    var mConnectionId: Int? = null
    private var mMaxFps: Double = 30.0
    private var mSettingsBitrate: Long = 0
    private var mCurrentBitrate: Long = 0
    private var mCurrentFps = 0.0
    private var mCurrentRange: FpsRange = FpsRange(30, 30)
    private var mFpsRanges: Array<FpsRange?> = emptyArray()
    private var mStreamer: LarixStreamer? = null
    private var mCheckTimer: Timer? = null
    private var mListener: Listener? = null

    data class LossHistory(var ts: Long, var audio: Long, var video: Long)
    data class BitrateHistory(var ts: Long, var bitrate: Long)

    fun setListener(listener: Listener) {
        this.mListener = listener
    }

    fun setBitrate(bitrate: Long) {
        this.mSettingsBitrate = bitrate
    }

    fun setFpsRanges(fpsRanges: Array<FpsRange?>) {
        mFpsRanges = fpsRanges
    }

    open fun start(streamer: LarixStreamer, connectionId: Int) {
        start(streamer, mSettingsBitrate, connectionId)
    }

    open fun start(streamer: LarixStreamer, bitrate: Long, connectionId: Int) {
        mStreamer = streamer
        mConnectionId = connectionId
        mLossHistory.clear()
        mBitrateHistory.clear()
        val currentMillis = System.currentTimeMillis()
        mLossHistory.add(LossHistory(currentMillis, 0, 0))
        mBitrateHistory.add(BitrateHistory(currentMillis, bitrate))
        mCurrentBitrate = bitrate
        mCurrentFps = mMaxFps
        runTask()
    }

    fun stop() {
        if (mFullBitrate > 0) {
            updateFps(mFullBitrate)
        }
        mCurrentBitrate = 0
        mStreamer = null
        mConnectionId = null
        mCheckTimer?.cancel()
    }

    fun pause() {
        mCheckTimer?.cancel()
    }

    private fun runTask() {
        if (checkDelay() == 0L || checkInterval() == 0L) return
        val checkNetwork: TimerTask = object : TimerTask() {
            override fun run() {
                try {
                    val connectionId = mConnectionId?: return
                    val streamer = mStreamer?: return

                    val audioLost: Long = streamer.getAudioPacketsLost(connectionId)
                    var videoLost: Long = streamer.getVideoPacketsLost(connectionId)
                    videoLost += streamer.getUdpPacketsLost(connectionId)
                    check(audioLost, videoLost)
                } catch (ignored: Exception) {}
            }
        }
        mCheckTimer = Timer()
        mCheckTimer?.schedule(checkNetwork, checkDelay(), checkInterval())
    }

    fun resume() {
        if (mCurrentBitrate == 0L) {
            return
        }
        mCurrentBitrate = mFullBitrate
        mMaxFps = 30.0
        mCurrentRange = FpsRange(30, 30)
        mCurrentFps = mMaxFps
        runTask()
        mStreamer?.changeBitRate(mFullBitrate.toInt())
    }

    fun setConnection(connectionId: Int) {
        mConnectionId = connectionId
    }

    fun getCurrentBitrate(): Long = mCurrentBitrate

    open fun check(audioLost: Long, videoLost: Long) {}

    protected fun countLostForInterval(interval: Long): Long {
        var lostPackets: Long = 0
        val last = mLossHistory.lastElement()
        for (i in mLossHistory.indices.reversed()) {
            if (mLossHistory.elementAt(i).ts < interval) {
                val h = mLossHistory.elementAt(i)
                lostPackets = last.video - h.video + (last.audio - h.audio)
                break
            }
        }
        return lostPackets
    }

    fun changeBitrate(newBitrate: Long) {
        val curTime = System.currentTimeMillis()
        mBitrateHistory.add(BitrateHistory(curTime, newBitrate))
        updateFps(newBitrate)
        mStreamer?.changeBitRate(newBitrate.toInt())
        mCurrentBitrate = newBitrate
        mListener?.onChangeBitrate(mCurrentBitrate)
    }

    fun changeBitrateQuiet(newBitrate: Long) {
        mStreamer?.changeBitRate(newBitrate.toInt())
    }

    open fun checkInterval(): Long = 500

    open fun checkDelay(): Long = 1000

    private fun updateFps(newBitrate: Long) {
        if (mFpsRanges.isEmpty()) return
        val bitrateRel = newBitrate * 1.0 / mFullBitrate
        var relFps = mMaxFps
        if (bitrateRel < 0.5) {
            relFps = 15.0.coerceAtLeast(floor(mMaxFps * bitrateRel * 2.0 / 5.0) * 5.0)
        }
        if (abs(relFps - mCurrentFps) < 1.0) return
        mCurrentFps = relFps
        val newRange: FpsRange? = nearestFpsRange(mFpsRanges, relFps.roundToLong(), false)
        if (newRange?.fpsMax == mCurrentRange.fpsMax && newRange.fpsMin == mCurrentRange.fpsMin) {
            return
        }
        mStreamer?.changeFpsRange(newRange)
        newRange?.let { mCurrentRange = it }
    }

    // Find best matching FPS range
    // (fpsMax is much important to be closer to target, so we squared it)
    // In strict mode targetFps will be exact within range, otherwise just as close as possible
    open fun nearestFpsRange(
        fpsRanges: Array<FpsRange?>,
        targetFps: Long,
        strict: Boolean
    ): FpsRange? {
        //Find best matching FPS range
        // (fpsMax is much important to be closer to target, so we squared it)
        var minDistance = 1e10f
        var range: FpsRange? = FpsRange(0, 0)
        for (r in fpsRanges) {
            if (r == null) continue
            if (strict && (r.fpsMin > targetFps || r.fpsMax < targetFps)) {
                continue
            }
            val distance = (r.fpsMax - targetFps) * (r.fpsMax - targetFps) + abs(r.fpsMin - targetFps)
            if (distance < minDistance) {
                range = r
                if (distance < 0.01f) {
                    break
                }
                minDistance = distance.toFloat()
            }
        }
        return range
    }

    interface Listener {
        fun onChangeBitrate(lastBitrate: Long)
    }

    companion object {

        fun instance(
            bitrate: Long,
            bitrateMode: BitrateMode,
            fpsRanges: Array<FpsRange?>
        ): BitrateAdapter {
            return if (bitrateMode == BitrateMode.LadderAscend) {
                BitrateLadderAscendMode().apply {
                    this.setBitrate(bitrate)
                    this.setFpsRanges(fpsRanges)
                }
            } else {
                LogarithmicDescendMode().apply {
                    this.setBitrate(bitrate)
                    this.setFpsRanges(fpsRanges)
                }
            }
        }

    }

}