package com.tokopedia.play.data.sse

import android.util.Log
import com.tokopedia.authentication.HEADER_RELEASE_TRACK
import com.tokopedia.config.GlobalConfig
import com.tokopedia.play_common.sse.OkSse
import com.tokopedia.play_common.sse.ServerSentEvent
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 08, 2021
 */
class PlayChannelSSE @Inject constructor(
    private val userSession: UserSessionInterface
) {
    private var sse: ServerSentEvent? = null

    fun connect(channelId: String, pageSource: String, gcToken: String) {
        var url = "https://sse.tokopedia.com/play-sse?page=$pageSource&channel_id=$channelId"
        if(gcToken.isNotEmpty()) url += "&token=${gcToken}"

        val request = Request.Builder().get().url(url)
            .header("Origin", TokopediaUrl.getInstance().WEB)
            .header("Accounts-Authorization", "Bearer ${userSession.accessToken}")
            .header("X-Device", "android-" + GlobalConfig.VERSION_NAME)
            .header(HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX)
            .build()

        val okSSE = OkSse()
        sse = okSSE.newServerSentEvent(request, object: ServerSentEvent.Listener {
            override fun onOpen(sse: ServerSentEvent, response: Response) {
                Log.d("<SSE>", "onOpen")
            }

            override fun onMessage(
                sse: ServerSentEvent,
                id: String,
                event: String,
                message: String
            ) {
                Log.d("<SSE>", "onMessage - event: $event & message: $message")
            }

            override fun onComment(sse: ServerSentEvent, comment: String) {
                Log.d("<SSE>", "onComment - event: $comment")
            }

            override fun onRetryTime(sse: ServerSentEvent, milliseconds: Long): Boolean {
                Log.d("<SSE>", "onRetryTime - milliseconds: $milliseconds")
                return false
            }

            override fun onRetryError(
                sse: ServerSentEvent,
                throwable: Throwable,
                response: Response?
            ): Boolean {
                Log.d("<SSE>", "onRetryError - response: $response")
                return false
            }

            override fun onClosed(sse: ServerSentEvent) {
                Log.d("<SSE>", "onClosed")
            }

            override fun onPreRetry(sse: ServerSentEvent, originalRequest: Request): Request? {
                Log.d("<SSE>", "onPreRetry - originalRequest: $request")
                return null
            }
        })
    }

    fun close() {
        sse?.close()
    }
}