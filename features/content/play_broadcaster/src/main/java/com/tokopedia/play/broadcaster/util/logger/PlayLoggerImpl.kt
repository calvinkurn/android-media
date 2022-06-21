package com.tokopedia.play.broadcaster.util.logger

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.play.broadcaster.data.type.PlaySocketType
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.pusher.statistic.PlayBroadcasterMetric
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


/**
 * Created by mzennis on 21/10/21.
 */
class PlayLoggerImpl @Inject constructor(
    private val collector: PlayLoggerCollector,
    private val remoteConfig: RemoteConfig,
    private val userSession: UserSessionInterface,
    ) : PlayLogger {

    private val isMonitoringLogEnabled: Boolean
        get() {
            val arrOfPostFix = remoteConfig.getLong(FIREBASE_REMOTE_CONFIG_KEY_BRO_MONITORING, 0)
            return if(!userSession.isLoggedIn) false
            else arrOfPostFix.toString().toCharArray().any {
                userSession.userId.toString().toCharArray().last() == it
            }
        }

    companion object {
        const val LIMIT_LOG = 20
        private const val TAG_SCALYR = "PLAY_BROADCASTER"
        private const val TAG_PLAY_BROADCASTER_MONITORING = "PLAY_BROADCASTER_MONITORING"
        private const val FIREBASE_REMOTE_CONFIG_KEY_BRO_MONITORING = "android_mainapp_play_broadcaster_monitoring"
    }

    private fun sendLog(messages: Map<String, String>) {
        if(!isMonitoringLogEnabled) return
        ServerLogger.log(Priority.P2, TAG_PLAY_BROADCASTER_MONITORING, messages)
    }

    override fun logChannelStatus(channelStatus: ChannelStatus) {
        collector.collect(
            Pair("channel", channelStatus.value)
        )
    }

    override fun logPusherState(pusherState: PlayBroadcasterState) {
        collector.collect(
            Pair("pusher", pusherState.toString())
        )
    }

    override fun logSocketType(socketType: PlaySocketType) {
        collector.collect(
            Pair("socket", socketType.type.value)
        )
    }

    override fun sendAll(channelId: String) {
        collector.getAll().chunked(LIMIT_LOG).forEach {
            sendLog(
                mapOf(
                    "channel_id" to channelId,
                    "log_trace" to it.toString()
                )
            )
            collector.getAll().removeAll(it)
        }
    }

    override fun sendBroadcasterLog(metric: PlayBroadcasterMetric) {
        val metrics = mapOf(
            "videoBitrate" to "${metric.videoBitrate}",
            "audioBitrate" to "${metric.audioBitrate}",
            "resolution" to metric.resolution,
            "bandwidth" to "${metric.bandwidth}",
            "fps" to "${metric.fps}",
            "traffic" to "${metric.traffic}",
            "videoBufferTimestamp" to "${metric.videoBufferTimestamp}",
            "channelID" to metric.channelId,
            "authorID" to metric.authorId,
        )
        ServerLogger.log(Priority.P2, TAG_PLAY_BROADCASTER_MONITORING, metrics)
    }
}