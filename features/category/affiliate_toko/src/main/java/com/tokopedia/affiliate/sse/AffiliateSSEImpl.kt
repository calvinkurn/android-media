package com.tokopedia.affiliate.sse

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.affiliate.sse.model.AffiliateSSEAction
import com.tokopedia.affiliate.sse.model.AffiliateSSECloseReason
import com.tokopedia.affiliate.sse.model.AffiliateSSEResponse
import com.tokopedia.analyticsdebugger.debugger.SSELogger
import com.tokopedia.sse.OkSse
import com.tokopedia.sse.ServerSentEvent
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AffiliateSSEImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    @ApplicationContext private val context: Context
) : AffiliateSSE {

    private var sse: ServerSentEvent? = null
    private var sseFlow = MutableSharedFlow<AffiliateSSEAction>(extraBufferCapacity = 100)

    override fun connect(pageSource: String) {
        SSELogger.getInstance(context)
            .init(buildGeneralInfo(userSession.gcToken, pageSource).toString())
        SSELogger.getInstance(context).send("SSE Connecting...")

        val url = "https://sse-staging.tokopedia.com/$AFFILIATE_SSE?page_source=$pageSource"

        val request = Request.Builder().get().url(url)
            .header("Origin", TokopediaUrl.getInstance().WEB)
            .header("Authorization", "Bearer ${userSession.accessToken}")
            .build()

        sse = OkSse().newServerSentEvent(
            request,
            object : ServerSentEvent.Listener {
                override fun onOpen(sse: ServerSentEvent, response: Response) {
                    SSELogger.getInstance(context).send("SSE Open")
                }

                override fun onMessage(
                    sse: ServerSentEvent,
                    id: String,
                    event: String,
                    message: String
                ) {
                    sseFlow.tryEmit(
                        AffiliateSSEAction.Message(
                            AffiliateSSEResponse(
                                event = event,
                                message = message
                            )
                        )
                    )
                    SSELogger.getInstance(context).send(event, message)
                }

                override fun onComment(sse: ServerSentEvent, comment: String) = Unit

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
                    SSELogger.getInstance(context).send("SSE Closed")
                    sseFlow.tryEmit(AffiliateSSEAction.Close(AffiliateSSECloseReason.INTENDED))
                }

                override fun onPreRetry(sse: ServerSentEvent, originalRequest: Request): Request {
                    return request
                }
            }
        )
    }

    override fun close() {
        sse?.close()
    }

    override fun listen(): Flow<AffiliateSSEAction> {
        return sseFlow.filterNotNull().buffer().flowOn(dispatchers.io)
    }

    private fun buildGeneralInfo(
        gcToken: String,
        pageSource: String
    ): Map<String, String> {
        return mapOf(
            "Authorization" to gcToken.ifEmpty { "\"\"" },
            "gcToken" to gcToken.ifEmpty { "\"\"" },
            "pageSource" to pageSource.ifEmpty { "\"\"" }
        )
    }

    private companion object {
        const val AFFILIATE_SSE = "affiliate/sse/connect"
    }
}
