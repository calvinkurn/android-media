package com.tokopedia.sellerhomecommon.sse

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.config.GlobalConfig
import com.tokopedia.sellerhomecommon.sse.model.WidgetSSEModel
import com.tokopedia.sse.OkSse
import com.tokopedia.sse.ServerSentEvent
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import okhttp3.Request
import okhttp3.Response

/**
 * Created by @ilhamsuaib on 10/10/22.
 */

class SellerHomeWidgetSSEImpl(
    private val dispatchers: CoroutineDispatchers,
    private val userSession: UserSessionInterface
) : SellerHomeWidgetSSE {

    companion object {
        private const val DATA_KEY_SEPARATOR = ","
        private const val HEADER_AUTHORIZATION = "Accounts-Authorization"
        private const val HEADER_X_DEVICE = "X-Device"
        private const val BEARER = "Bearer %s"
        private const val ANDROID_VERSION = "android-%s"
    }

    private var sse: ServerSentEvent? = null
    private var sseFlow = MutableSharedFlow<WidgetSSEModel>(extraBufferCapacity = 100)

    override fun connect(page: String, dataKeys: List<String>) {
        printLog("SSE Connecting...")

        val baseSseUrl = getBaseSseUrl()
        val dataKey = dataKeys.joinToString(DATA_KEY_SEPARATOR)
        val url = String.format(baseSseUrl, page, dataKey)
        val authorization = String.format(BEARER, userSession.accessToken)
        val xDevice = String.format(ANDROID_VERSION, GlobalConfig.VERSION_NAME)

        val request = Request.Builder().get().url(url)
            .addHeader(HEADER_X_DEVICE, xDevice)
            .addHeader(HEADER_AUTHORIZATION, authorization)
            .build()

        closeSse()
        sse = OkSse().newServerSentEvent(request, getSseEventListener(request))
    }

    override fun closeSse() {
        sse?.close()
    }

    override fun listen(): Flow<WidgetSSEModel> {
        return sseFlow.filterNotNull().buffer().flowOn(dispatchers.io)
    }

    private fun getBaseSseUrl(): String {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            "https://sse-staging.tokopedia.com/seller-dashboard/sse/datakeys?page=%s&datakeys=%s"
        } else {
            "https://sse.tokopedia.com/seller-dashboard/sse/datakeys?page=%s&datakeys=%s"
        }
    }

    private fun getSseEventListener(request: Request): ServerSentEvent.Listener {
        return object : ServerSentEvent.Listener {

            override fun onOpen(sse: ServerSentEvent, response: Response) {
                printLog("SellerHomeWidgetSSE : onOpen")
            }

            override fun onMessage(
                sse: ServerSentEvent,
                id: String,
                event: String,
                message: String
            ) {
                printLog("SellerHomeWidgetSSE : onMessage -> $event")
                sseFlow.tryEmit(WidgetSSEModel(event = event, message = message))
            }

            override fun onComment(sse: ServerSentEvent, comment: String) {
                printLog("SellerHomeWidgetSSE : onComment")
            }

            override fun onRetryTime(sse: ServerSentEvent, milliseconds: Long): Boolean {
                printLog("SellerHomeWidgetSSE : onRetryTime : $milliseconds")
                return true
            }

            override fun onRetryError(
                sse: ServerSentEvent,
                throwable: Throwable,
                response: Response?
            ): Boolean {
                printLog("SellerHomeWidgetSSE : onRetryError : ${throwable.message}")
                return true
            }

            override fun onClosed(sse: ServerSentEvent) {
                printLog("SellerHomeWidgetSSE : onClosed")
            }

            override fun onPreRetry(sse: ServerSentEvent, originalRequest: Request): Request {
                printLog("SellerHomeWidgetSSE : onPreRetry")
                return request
            }
        }
    }

    private fun printLog(s: String) {
        println(s)
    }
}
