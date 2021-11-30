package com.tokopedia.broadcaster.tracker

import com.tokopedia.broadcaster.uimodel.LoggerUIModel
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*
import kotlin.coroutines.CoroutineContext

interface BroadcasterTracker {
    fun priority(): Priority
    fun tag(): String
    fun track(data: LoggerUIModel)
    fun stopTrack()
}

class BroadcasterTrackerImpl : BroadcasterTracker, CoroutineScope {

    private val mTrackerData = mutableListOf<LoggerUIModel>()
    private var trackerTimer: Timer? = null

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    override fun priority() = Priority.P2

    override fun tag() = TAG

    override fun track(data: LoggerUIModel) {
        if (data.url.isEmpty()) return

        mTrackerData.add(data)

        trackerTimer = Timer()
        trackerTimer?.schedule(object : TimerTask() {
            override fun run() {
                launch {
                    sendToNewRelic()

                    withContext(Dispatchers.Main) {
                        mTrackerData.clear()
                    }
                }
            }

        }, DELAYED_TIME, PERIOD_TIME)
    }

    override fun stopTrack() {
        try {
            // cancel period task
            trackerTimer?.cancel()
            trackerTimer = null

            // cancel coroutines
            if (isActive) {
                cancel()
            }
        } catch (e: Exception) {}
    }

    private fun sendToNewRelic() {
        mTrackerData.forEach {
            ServerLogger.log(
                priority = priority(),
                tag = tag(),
                message = it.toMap()
            )
        }
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
        const val PERIOD_TIME = 5000L
    }

}