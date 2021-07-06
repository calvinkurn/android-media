package com.tokopedia.play.broadcaster.pusher.bitrate

import android.content.Context
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.Streamer.FpsRange
import java.util.*
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToLong


/**
 * Created by mzennis on 17/06/21.
 */
abstract class BitrateAdapter(context: Context) {

    companion object {

        fun newInstance(
            context: Context,
            bitrate: Long,
            fpsRanges: Array<FpsRange?>
        ): BitrateAdapter {
            return BitrateAdapterLadderAscendMode(context).apply {
                this.setBitrate(bitrate)
                this.setFpsRanges(fpsRanges)
            }
        }
    }

    protected var mContext: Context = context
    protected var mFullBitrate: Long = 0
    protected var mLossHistory: Vector<LossHistory> = Vector()
    protected var mBitrateHistory: Vector<BitrateHistory> = Vector()
    protected var mMaxFps: Double = 30.0

    private var mSettingsBitrate: Long = 0
    private var mCurrentBitrate: Long = 0
    private var mCurrentFps = 0.0
    private var mCurrentRange: FpsRange = FpsRange(30, 30)
    private var mFpsRanges: Array<FpsRange?> = emptyArray()
    private var mStreamer: Streamer? = null
    private var mConnectionId: Int? = null
    private var mCheckTimer: Timer? = null
    private var mListener: Listener? = null

    inner class LossHistory(var ts: Long, var audio: Long, var video: Long)
    inner class BitrateHistory(var ts: Long, var bitrate: Long)

    fun setListener(listener: Listener) {
        this.mListener = listener
    }

    fun setBitrate(bitrate: Long) {
        this.mSettingsBitrate = bitrate
    }

    fun setFpsRanges(fpsRanges: Array<FpsRange?>) {
        mFpsRanges = fpsRanges
    }

    open fun start(streamer: Streamer, connectionId: Int) {
        start(streamer, mSettingsBitrate, connectionId)
    }

    open fun start(streamer: Streamer, bitrate: Long, connectionId: Int) {
        mStreamer = streamer
        mConnectionId = connectionId
        mLossHistory.clear()
        mBitrateHistory.clear()
        val currentMillis = System.currentTimeMillis()
        mLossHistory.add(LossHistory(currentMillis, 0, 0))
        mBitrateHistory.add(BitrateHistory(currentMillis, bitrate.toLong()))
        mCurrentBitrate = bitrate
        mCurrentFps = mMaxFps
        runTask()
    }

    fun stop() {
        if (mFullBitrate > 0) {
            updateFps(mFullBitrate.toLong())
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
                if (mStreamer == null || mConnectionId == null) return
                val audioLost: Long = mStreamer!!.getAudioPacketsLost(mConnectionId!!)
                var videoLost: Long = mStreamer!!.getVideoPacketsLost(mConnectionId!!)
                videoLost += mStreamer!!.getUdpPacketsLost(mConnectionId!!)
                check(audioLost, videoLost)
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

    protected open fun check(audioLost: Long, videoLost: Long) {}
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

    protected fun changeBitrate(newBitrate: Long) {
        val curTime = System.currentTimeMillis()
        mBitrateHistory.add(BitrateHistory(curTime, newBitrate))
        updateFps(newBitrate)
        mStreamer?.changeBitRate(newBitrate.toInt())
        mCurrentBitrate = newBitrate
        mListener?.onChangeBitrate(mCurrentBitrate)
    }

    protected fun changeBitrateQuiet(newBitrate: Long) {
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
}
