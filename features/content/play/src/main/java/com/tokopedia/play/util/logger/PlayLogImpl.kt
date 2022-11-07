package com.tokopedia.play.util.logger

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play_common.util.PlayLiveRoomMetricsCommon
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject

/**
 * @author by astidhiyaa on 29/03/22
 *
 */
class PlayLogImpl @Inject constructor(private val logCollector: PlayLogCollector,
                                      private val remoteConfig: RemoteConfig,
                                      private val userSession: UserSessionInterface): PlayLog{

    private val isMonitoringLogEnabled: Boolean
        get() {
            val arrOfPostFix = remoteConfig.getLong(FIREBASE_REMOTE_CONFIG_KEY_VIEWER_MONITORING, 0)
            return if(!userSession.isLoggedIn || arrOfPostFix == 0L) false
            else arrOfPostFix.toString().toCharArray().any {
                userSession.userId.last() == it
            }
        }

    override fun logTimeToFirstByte(timeToFirstByte: Long) {
        logCollector.collect(
            Pair("timeToFirstByte", timeToFirstByte)
        )
    }

    override fun logDownloadSpeed(downloadSpeed: Float) {
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

    override fun sendAll(channelId: String, videoPlayer: PlayVideoPlayerUiModel) {
        if(!isMonitoringLogEnabled) return
        val url = when (videoPlayer) {
            is PlayVideoPlayerUiModel.General -> videoPlayer.params.videoUrl
            is PlayVideoPlayerUiModel.YouTube -> videoPlayer.youtubeId
            else -> ""
        }
        val mapped = hashMapOf("channel_id" to channelId, "url" to url)
        logCollector.getAll().chunked(LIMIT_LOG).forEach { logs ->
            logs.forEach {
                mapped[it.first] = it.second.toString()
            }
            sendLog(mapped)
            logCollector.getAll().removeAll(logs)
        }
    }

    private fun sendLog(messages: Map<String, String>) {
        ServerLogger.log(Priority.P2, PLAY_LOG_TAG, messages)
    }

    companion object{
        private const val PLAY_LOG_TAG = "PLAY_VIEWER_MONITORING"
        private const val LIMIT_LOG = 10
        private const val FIREBASE_REMOTE_CONFIG_KEY_VIEWER_MONITORING = "android_mainapp_play_viewer_monitoring"
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