package com.tokopedia.play.util.logger

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.play_common.util.PlayLiveRoomMetricsCommon
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject

/**
 * @author by astidhiyaa on 29/03/22
 *
 */
class PlayLogImpl @Inject constructor(private val logCollector: PlayLogCollector): PlayLog{

    override fun logTimeToFirstByte(timeToFirstByte: String) {
        logCollector.collect(
            Pair("timeToFirstByte", timeToFirstByte)
        )
    }

    override fun logDownloadSpeed(downloadSpeed: String) {
        logCollector.collect(
            Pair("downloadSpeed", downloadSpeed)
        )
    }

    //[(duration, timestamp),(duration, timestamp)]
    override fun logBufferEvent(bufferingEvent: PlayLiveRoomMetricsCommon.BufferEvent, bufferingCount: Int) {
        logCollector.collect(
            Pair("bufferingCount", bufferingCount.toString())
        )

        logCollector.addList(bufferingEvent)

        logCollector.collect(
            Pair("bufferingEvent", logCollector.getList())
        )
    }

    override fun logWatchingDuration(watchingTime: String) {
        logCollector.collect(
            Pair("watchingDuration", watchingTime)
        )
    }

    override fun sendAll(channelId: String, streamingUrl: String) {
        logCollector.getAll().chunked(LIMIT_LOG).forEach {
            sendLog(
                mapOf(
                    "channel_id" to channelId,
                    "url" to streamingUrl,
                    "log_trace" to it.toString()
                )
            )
            logCollector.getAll().removeAll(it)
        }
    }

    private fun sendLog(messages: Map<String, String>) {
        ServerLogger.log(Priority.P2, PLAY_LOG_TAG, messages)
    }

    companion object{
        private const val PLAY_LOG_TAG = "PlayViewerMonitoring"
        private const val LIMIT_LOG = 10
    }
}

class PlayLogCollector @Inject constructor(){
    private val logs = ConcurrentLinkedQueue<Pair<String, Any>>()
    private val listOfLogs = mutableListOf<Any>()

    fun collect(log: Pair<String, Any>) {
        logs.add(log)
    }

    fun getAll(): ConcurrentLinkedQueue<Pair<String, Any>> {
        return logs
    }

    fun addList(data: Any){
        listOfLogs.add(data)
    }

    fun getList() = listOfLogs
}