package com.tokopedia.sellerhomecommon.sse

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.config.GlobalConfig
import com.tokopedia.sellerhomecommon.sse.model.SSEModel
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

/**
 * Created by @ilhamsuaib on 10/10/22.
 */

class SellerHomeCommonImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
) : SellerHomeCommonSSE {

    companion object {
        private const val URL =
            "https://sse.tokopedia.com/seller-dashboard/sse/datakeys?page=%s&datakeys=%s"
        private const val DATA_KEY_SEPARATOR = ","
        private const val HEADER_ORIGIN = "Origin"
        private const val HEADER_AUTHORIZATION = "Accounts-Authorization"
        private const val HEADER_X_DEVICE = "X-Device"
        private const val BEARER = "Bearer %s"
        private const val ANDROID_VERSION = "android-%s"
    }

    private var sse: ServerSentEvent? = null
    private var sseFlow = MutableSharedFlow<SSEModel>(extraBufferCapacity = 100)

    override fun connect(page: String, dataKeys: List<String>) {
        val dataKey = dataKeys.joinToString(DATA_KEY_SEPARATOR)
        val url = String.format(URL, page, dataKey)

        val request = Request.Builder().get().url(url)
            .header(HEADER_ORIGIN, TokopediaUrl.getInstance().WEB)
            .header(HEADER_AUTHORIZATION, String.format(BEARER, userSession.accessToken))
            .header(HEADER_X_DEVICE, String.format(ANDROID_VERSION, GlobalConfig.VERSION_NAME))
            .build()

        val sseListener = getSseListener(request)
        sse = OkSse().newServerSentEvent(request, sseListener)
    }

    override fun close() {
        sse?.close()
    }

    override fun listen(): Flow<SSEModel> {
        return sseFlow.filterNotNull().buffer().flowOn(dispatchers.io)
    }

    private fun getSseListener(request: Request): ServerSentEvent.Listener {
        return object : ServerSentEvent.Listener {

            override fun onOpen(sse: ServerSentEvent, response: Response) {

            }

            override fun onMessage(
                sse: ServerSentEvent,
                id: String,
                event: String,
                message: String
            ) {
                sseFlow.tryEmit(SSEModel(event = event, message = message))
            }

            override fun onComment(sse: ServerSentEvent, comment: String) {}

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

            }

            override fun onPreRetry(sse: ServerSentEvent, originalRequest: Request): Request {
                return request
            }
        }
    }
}
