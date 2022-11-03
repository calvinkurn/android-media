package com.tokopedia.sellerhomecommon.sse

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.debugger.SSELogger
import com.tokopedia.config.GlobalConfig
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import com.tokopedia.sellerhomecommon.sse.mapper.WidgetSSEMapper
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
import kotlinx.coroutines.flow.map
import okhttp3.Request
import okhttp3.Response

/**
 * Created by @ilhamsuaib on 10/10/22.
 */

class SellerHomeWidgetSSEImpl(
    private val context: Context,
    private val userSession: UserSessionInterface,
    private val widgetSseMapper: WidgetSSEMapper,
    private val dispatchers: CoroutineDispatchers
) : SellerHomeWidgetSSE {

    companion object {
        private const val LOG_KEY = "SellerHomeWidgetSSE"
        private const val DATA_KEY_SEPARATOR = ","
        private const val HEADER_AUTHORIZATION = "Accounts-Authorization"
        private const val HEADER_X_DEVICE = "X-Device"
        private const val BEARER = "Bearer %s"
        private const val ANDROID_VERSION = "android-%s"
    }

    private var sse: ServerSentEvent? = null
    private var sseFlow = MutableSharedFlow<WidgetSSEModel>(extraBufferCapacity = 100)

    override fun connect(page: String, dataKeys: List<String>) {
        initLogger()
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

    override fun listen(): Flow<BaseDataUiModel?> {
        return sseFlow.filterNotNull()
            .buffer()
            .flowOn(dispatchers.io)
            .map {
                widgetSseMapper.mappingWidget(it.event, it.message)
            }
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
                printLog("onOpen")
            }

            override fun onMessage(
                sse: ServerSentEvent,
                id: String,
                event: String,
                message: String
            ) {
                printLog("onMessage -> $event -> $message")
                if (widgetSseMapper.getStatusIsValidDataKey(event)) {
                    sseFlow.tryEmit(WidgetSSEModel(event = event, message = message))
                }
            }

            override fun onComment(sse: ServerSentEvent, comment: String) {
                printLog("onComment")
            }

            override fun onRetryTime(sse: ServerSentEvent, milliseconds: Long): Boolean {
                printLog("onRetryTime : $milliseconds")
                return true
            }

            override fun onRetryError(
                sse: ServerSentEvent,
                throwable: Throwable,
                response: Response?
            ): Boolean {
                printLog("onRetryError : ${throwable.message}")
                return true
            }

            override fun onClosed(sse: ServerSentEvent) {
                printLog("onClosed")
            }

            override fun onPreRetry(sse: ServerSentEvent, originalRequest: Request): Request {
                printLog("onPreRetry")
                return request
            }
        }
    }

    private fun initLogger() {
        SSELogger.getInstance(context).init(LOG_KEY)
    }

    private fun printLog(s: String) {
        SSELogger.getInstance(context).send(s)
        println("$LOG_KEY : $s")
    }
}
