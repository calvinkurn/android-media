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
class PlayLoggerImpl @Inject constructor() : PlayLogger {

    companion object {
        private const val LIMIT_LOG = 20
        private const val PLAY_BROADCASTER = "ANDROID_PLAY_BRO"
    }

    private val logs = mutableListOf<String>()

    private fun sendLog(messages: Map<String, String>) {
        ServerLogger.log(Priority.P2, PLAY_BROADCASTER, messages)
    }

    override fun logChannelStatus(channelStatus: PlayChannelStatusType) {
        logs.add(
            buildString {
                appendLine("time: ${System.currentTimeMillis()}")
                appendLine("channel status: ${channelStatus.value}")
            }
        )
    }

    override fun logPusherStatus(pusherState: PlayLivePusherMediatorState) {
        val status = when(pusherState) {
            PlayLivePusherMediatorState.Connecting -> "CONNECTING"
            is PlayLivePusherMediatorState.Error -> "ERROR: \ntype:${pusherState.error.type}\nreason:${pusherState.error.reason}"
            PlayLivePusherMediatorState.Paused -> "PAUSED"
            PlayLivePusherMediatorState.Recovered -> "RECOVERED"
            is PlayLivePusherMediatorState.Resume -> if (pusherState.isResumed) "RESUMED" else "RESUME"
            PlayLivePusherMediatorState.Started -> "STARTED"
            is PlayLivePusherMediatorState.Stopped -> "STOPPED"
            PlayLivePusherMediatorState.Idle -> "IDLE"
        }
        logs.add(
            buildString {
                appendLine("time: ${System.currentTimeMillis()}")
                appendLine("pusher status: $status")
                appendLine()
            }
        )
    }

    override fun logSocketStatus(socketType: PlaySocketType) {
        logs.add(
            buildString {
                appendLine("time: ${System.currentTimeMillis()}")
                appendLine("socket status: ${socketType.type.value}")
            }
        )
    }

    override fun sendAll(channelId: String) {
        if (logs.isEmpty()) return
        while (logs.size > 0) {
            val logTrace = if (logs.size > LIMIT_LOG) logs.take(LIMIT_LOG) else logs
            sendLog(
                mapOf(
                    "channel_id" to channelId,
                    "log_trace" to logTrace.toString()
                )
            )
            logs.removeAll(logTrace)
        }
    }
}