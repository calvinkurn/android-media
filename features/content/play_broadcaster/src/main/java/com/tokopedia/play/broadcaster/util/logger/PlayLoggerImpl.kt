package com.tokopedia.play.broadcaster.util.logger

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.play.broadcaster.data.type.PlaySocketType
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorState
import com.tokopedia.play_common.types.PlayChannelStatusType
import javax.inject.Inject


/**
 * Created by mzennis on 21/10/21.
 */
class PlayLoggerImpl @Inject constructor(
    private val collector: PlayLoggerCollector
) : PlayLogger {

    companion object {
        const val LIMIT_LOG = 20
        private const val TAG_SCALYR = "PLAY_BROADCASTER"
    }

    private fun sendLog(messages: Map<String, String>) {
        ServerLogger.log(Priority.P2, TAG_SCALYR, messages)
    }

    override fun logChannelStatus(channelStatus: PlayChannelStatusType) {
        collector.collect(
            Pair("channel", channelStatus.value)
        )
    }

    override fun logPusherState(pusherState: PlayLivePusherMediatorState) {
        collector.collect(
            Pair("pusher", pusherState.tag)
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
}