package com.tokopedia.broadcaster.tracker

import com.tokopedia.broadcaster.data.uimodel.LoggerUIModel
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import java.util.*

interface BroadcasterTracker {
    fun priority(): Priority
    fun tag(): String
    fun track(interval: Int, data: LoggerUIModel)
    fun stopTrack()
}

class BroadcasterTrackerImpl : BroadcasterTracker {

    private var trackerTimer: Timer? = null

    override fun priority() = Priority.P2

    override fun tag() = TAG

    override fun track(interval: Int, data: LoggerUIModel) {
        if (data.url.isEmpty()) return

        trackerTimer = Timer()
        trackerTimer?.schedule(object : TimerTask() {
            override fun run() {
                ServerLogger.log(
                    priority = priority(),
                    tag = tag(),
                    message = data.toMap()
                )
            }

        }, DELAYED_TIME, interval.toLong())
    }

    override fun stopTrack() {
        try {
            // cancel period task
            trackerTimer?.cancel()
            trackerTimer = null
        } catch (e: Exception) {}
    }

    private fun LoggerUIModel.toMap(): Map<String, String> {
        return mapOf(
            "url" to url,
            "connectionId" to connectionId.toString(),
            "startTime" to startTime.toString(),
            "endTime" to endTime.toString(),
            "videoWidth" to videoWidth.toString(),
            "videoHeight" to videoHeight.toString(),
            "videoBitrate" to videoBitrate.toString(),
            "audioType" to audioType.status,
            "audioRate" to audioRate.toString(),
            "bitrateMode" to bitrateMode.status,
            "fps" to fps,
            "bandwidth" to bandwidth,
            "traffic" to traffic,
            "isPacketLossIncreasing" to isPacketLossIncreasing.toString(),
        )
    }

    companion object {
        private const val TAG = "LIVE_BROADCASTER"

        const val DELAYED_TIME = 1000L
    }

}