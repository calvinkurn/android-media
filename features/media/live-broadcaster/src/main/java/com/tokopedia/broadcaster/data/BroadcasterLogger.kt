package com.tokopedia.broadcaster.data

import com.wmspanel.libstream.Streamer
import java.util.*
import kotlin.math.ceil

class BroadcasterLogger {

    private var mStreamer: Streamer? = null
    private var mConnectionId: Int? = null
    private var mStartTime: Long = 0
    private var mPrevTime: Long = 0
    private var mPrevBytes: Long = 0
    private var mBps: Long = 0
    private var mFps: Double = 0.0
    private var mVideoPacketsLost: Long = 0
    private var mAudioPacketsLost: Long = 0
    private var mUdpPacketsLost: Long = 0
    private var mPacketLossIncreased = false

    private val mLocale = Locale.getDefault()

    private fun Int?.orZero(): Int {
        if (this == null) return 0
        return this
    }

    private fun Long?.orZero(): Long {
        if (this == null) return 0
        return this
    }

    fun init(streamer: Streamer?, connectionId: Int?) {
        mStreamer = streamer
        mConnectionId = connectionId
        val time = System.currentTimeMillis()
        mStartTime = time
        mPrevTime = time
        mPrevBytes = mStreamer?.getBytesSent(mConnectionId.orZero()).orZero()
    }

    fun getBandwidth(): String {
        return when {
            mBps < 1000 -> String.format(mLocale, "%4dbps", mBps)
            mBps < 1000 * 1000 -> String.format(mLocale, "%3.1fKbps", mBps.toDouble() / 1000)
            mBps < 1000 * 1000 * 1000 -> String.format(mLocale, "%3.1fMbps", mBps.toDouble() / (1000 * 1000))
            else -> String.format(mLocale, "%3.1fGbps", mBps.toDouble() / (1000 * 1000 * 1000))
        }
    }

    fun getTraffic(): String {
        return when {
            mPrevBytes < 1024 -> String.format(mLocale, "%4dB", mPrevBytes)
            mPrevBytes < 1024 * 1024 -> String.format(mLocale, "%3.1fKB", mPrevBytes.toDouble() / 1024)
            mPrevBytes < 1024 * 1024 * 1024 -> String.format(mLocale, "%3.1fMB", mPrevBytes.toDouble() / (1024 * 1024))
            else -> String.format(mLocale, "%3.1fGB", mPrevBytes.toDouble() / (1024 * 1024 * 1024))
        }
    }

    fun getFps(): String {
        val fps = ceil(mFps).toInt()
        return String.format("%d fps", fps)
    }

    fun isPacketLossIncreasing(): Boolean = mPacketLossIncreased

    fun update() {
        val streamer = mStreamer ?: return
        val connectionId = mConnectionId ?: return

        val currentTime = System.currentTimeMillis()
        val bytesSent = streamer.getBytesSent(connectionId)
        val timeDiff = currentTime - mPrevTime
        mBps = (if (timeDiff > 0) 8 * 1000 * (bytesSent - mPrevBytes) / timeDiff else 0)
        mPrevTime = currentTime
        mPrevBytes = bytesSent
        mPacketLossIncreased = false
        mFps = streamer.fps
        val audioPacketLost = streamer.getAudioPacketsLost(connectionId)
        val videoPacketLost = streamer.getAudioPacketsLost(connectionId)
        val udpPacketsLost = streamer.getAudioPacketsLost(connectionId)
        if (mAudioPacketsLost != audioPacketLost || mVideoPacketsLost != videoPacketLost || mUdpPacketsLost != udpPacketsLost) {
            mAudioPacketsLost = audioPacketLost
            mVideoPacketsLost = videoPacketLost
            mUdpPacketsLost = udpPacketsLost
            mPacketLossIncreased = true
        }
    }

}