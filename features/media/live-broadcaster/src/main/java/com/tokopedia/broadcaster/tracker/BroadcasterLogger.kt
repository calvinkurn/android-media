package com.tokopedia.broadcaster.tracker

import com.tokopedia.broadcaster.chucker.ui.uimodel.ChuckerLogUIModel
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

interface BroadcasterLogger {
    fun priority(): Priority
    fun tag(): String
    fun track(data: ChuckerLogUIModel)
}

class BroadcasterLoggerImpl : BroadcasterLogger {

    override fun priority(): Priority {
        return Priority.P2
    }

    override fun tag(): String {
        return "LIVE_BROADCASTER"
    }

    override fun track(data: ChuckerLogUIModel) {
        if (data.url.isEmpty()) return

        // track to new relic
        ServerLogger.log(
            priority = priority(),
            tag = tag(),
            message = data.toMap()
        )
    }

    private fun ChuckerLogUIModel.toMap(): Map<String, String> {
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
            "traffic" to traffic
        )
    }

}