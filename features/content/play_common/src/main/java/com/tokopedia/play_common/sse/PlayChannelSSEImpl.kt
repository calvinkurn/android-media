package com.tokopedia.play_common.sse

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.authentication.HEADER_RELEASE_TRACK
import com.tokopedia.config.GlobalConfig
import com.tokopedia.play_common.sse.base.OkSse
import com.tokopedia.play_common.sse.base.ServerSentEvent
import com.tokopedia.play_common.sse.model.SSEAction
import com.tokopedia.play_common.sse.model.SSECloseReason
import com.tokopedia.play_common.sse.model.SSEResponse
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.*
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 08, 2021
 */
class PlayChannelSSEImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
): PlayChannelSSE {

    private var sse: ServerSentEvent? = null
    private var sseFlow = MutableSharedFlow<SSEAction>(extraBufferCapacity = 100)

    override fun connect(channelId: String, pageSource: String, gcToken: String) {
        var url = "${TokopediaUrl.getInstance().SSE}${PLAY_SSE}?page=$pageSource&channel_id=$channelId"
        if(gcToken.isNotEmpty()) url += "&token=${gcToken}"

        val request = Request.Builder().get().url(url)
            .header("Origin", TokopediaUrl.getInstance().WEB)
            .header("Accounts-Authorization", "Bearer ${userSession.accessToken}")
            .header("X-Device", "android-" + GlobalConfig.VERSION_NAME)
            .header(HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX)
            .build()

        sse = OkSse().newServerSentEvent(request, object: ServerSentEvent.Listener {
            override fun onOpen(sse: ServerSentEvent, response: Response) { }

            override fun onMessage(
                sse: ServerSentEvent,
                id: String,
                event: String,
                message: String
            ) {
                sseFlow.tryEmit(SSEAction.Message(SSEResponse(event = event, message = message)))
            }

            override fun onComment(sse: ServerSentEvent, comment: String) { }

            override fun onRetryTime(sse: ServerSentEvent, milliseconds: Long): Boolean {
                return true
            }

            override fun onRetryError(
                sse: ServerSentEvent,
                throwable: Throwable,
                response: Response?
            ): Boolean {
                return true
            }

            override fun onClosed(sse: ServerSentEvent) {
                sseFlow.tryEmit(SSEAction.Close(SSECloseReason.INTENDED))
            }

            override fun onPreRetry(sse: ServerSentEvent, originalRequest: Request): Request? {
                return request
            }
        })
    }

    override fun close() {
        sse?.close()
    }

    override fun listen(): Flow<SSEAction> {
        return sseFlow.filterNotNull().buffer().flowOn(dispatchers.io)
    }

    private companion object {
        const val PLAY_SSE = "play-sse"
    }
}