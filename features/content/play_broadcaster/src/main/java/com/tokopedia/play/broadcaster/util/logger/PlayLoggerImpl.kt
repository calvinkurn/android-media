package com.tokopedia.play.broadcaster.util.logger

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.play.broadcaster.data.type.PlaySocketType
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.pusher.statistic.PlayBroadcasterMetric
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import java.lang.Exception
import javax.inject.Inject


/**
 * Created by mzennis on 21/10/21.
 */
class PlayLoggerImpl @Inject constructor(
    private val collector: PlayLoggerCollector
) : PlayLogger {

    companion object {
        const val LIMIT_LOG = 20
        private const val TAG_PLAY_BROADCASTER = "PLAY_BROADCASTER"
        private const val TAG_PLAY_BROADCASTER_MONITORING = "PLAY_BROADCASTER_MONITORING"

        private const val CHANNEL_ID_TAG = "channelID"
        private const val LOG_TRACE_TAG = "log_trace"
        private const val VIDEO_BITRATE_TAG = "videoBitrate"
        private const val AUDIO_BITRATE_TAG = "audioBitrate"
        private const val RESOLUTION_TAG = "resolution"
        private const val BANDWIDTH_TAG = "bandwidth"
        private const val FPS_TAG = "fps"
        private const val TRAFFIC_TAG = "traffic"
        private const val VIDEO_BUFFER_TIMESTAMP_TAG = "videoBufferTimestamp"
        private const val AUTHOR_ID_TAG = "authorID"

        private const val CHANNEL_TAG = "channel"
        private const val PUSHER_TAG = "pusher"
        private const val SOCKET_TAG = "socket"
        private const val ERROR_TAG = "error"
    }

    /***
     * Send logs to PLAY_BROADCASTER
     */
    private fun sendLog(messages: Map<String, String>) {
        ServerLogger.log(Priority.P2, TAG_PLAY_BROADCASTER, messages)
    }

    override fun logChannelStatus(channelStatus: ChannelStatus) {
        collector.collect(
            Pair(CHANNEL_TAG, channelStatus.value)
        )
    }

    override fun logPusherState(pusherState: PlayBroadcasterState) {
        collector.collect(
            Pair(PUSHER_TAG, pusherState.tag)
        )
    }

    override fun logSocketType(socketType: PlaySocketType) {
        collector.collect(
            Pair(SOCKET_TAG, socketType.type.value)
        )
    }

    override fun logBroadcastError(throwable: Throwable) {
        val errMessage = throwable.localizedMessage ?: return
        collector.collect(
            Pair(ERROR_TAG, errMessage)
        )
    }

    override fun sendAll(channelId: String) {
        try {
            collector.getAll().chunked(LIMIT_LOG).forEach {
                sendLog(
                    mapOf(
                        CHANNEL_ID_TAG to channelId,
                        LOG_TRACE_TAG to it.toString()
                    )
                )
                collector.getAll().removeAll(it)
            }
        } catch (ignored: Exception) {
        }
    }

    /***
     * Send logs to PLAY_BROADCASTER_MONITORING
     */
    override fun sendBroadcasterLog(metric: PlayBroadcasterMetric) {
        val metrics = mapOf(
            VIDEO_BITRATE_TAG to "${metric.videoBitrate}",
            AUDIO_BITRATE_TAG to "${metric.audioBitrate}",
            RESOLUTION_TAG to metric.resolution,
            BANDWIDTH_TAG to "${metric.bandwidth}",
            FPS_TAG to "${metric.fps}",
            TRAFFIC_TAG to "${metric.traffic}",
            VIDEO_BUFFER_TIMESTAMP_TAG to "${metric.videoBufferTimestamp}",
            CHANNEL_ID_TAG to metric.channelId,
            AUTHOR_ID_TAG to metric.authorId,
        )
        ServerLogger.log(Priority.P2, TAG_PLAY_BROADCASTER_MONITORING, metrics)
    }
}
