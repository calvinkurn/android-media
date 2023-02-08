package com.tokopedia.sellerhomecommon.sse

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.debugger.SSELogger
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.ZERO
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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * Created by @ilhamsuaib on 10/10/22.
 */

class SellerHomeWidgetSSEImpl(
    private val context: Context,
    private val userSession: UserSessionInterface,
    private val widgetSseMapper: WidgetSSEMapper,
    private val sseOkHttpClient: OkHttpClient,
    private val dispatchers: CoroutineDispatchers
) : SellerHomeWidgetSSE {

    companion object {
        private const val LOG_KEY = "SellerHomeWidgetSSE"
        private const val DATA_KEY_SEPARATOR = ","
        private const val HEADER_AUTHORIZATION = "Accounts-Authorization"
        private const val HEADER_X_DEVICE = "X-Device"
        private const val BEARER = "Bearer %s"
        private const val ANDROID_VERSION = "android-%s"
        private const val SSE_STAGING_URL =
            "https://sse-staging.tokopedia.com/seller-dashboard/sse/datakeys?page=%s&datakeys=%s"
        private const val SSE_PRODUCTION_URL =
            "https://sse.tokopedia.com/seller-dashboard/sse/datakeys?page=%s&datakeys=%s"
        private const val MAX_RETRY_COUNT = 10
    }

    private var isSseConnected = false
    private var retryCount = Int.ZERO
    private var sse: ServerSentEvent? = null
    private var sseFlow = MutableSharedFlow<WidgetSSEModel>(extraBufferCapacity = 100)

    override fun connect(page: String, dataKeys: List<String>) {
        if (isSseConnected) {
            return
        }
        isSseConnected = true

        initLogger()

        val baseSseUrl = getBaseSseUrl()
        val dataKey = dataKeys.joinToString(DATA_KEY_SEPARATOR)
        val url = String.format(baseSseUrl, page, dataKey)

        closeSse()
        printLog("SSE Connecting...$url")

        sse = OkSse(sseOkHttpClient).newServerSentEvent(getRequest(url), getSseEventListener(url))
    }

    override fun closeSse() {
        sse?.close()
        isSseConnected = false
    }

    override fun listen(): Flow<BaseDataUiModel?> {
        return sseFlow.filterNotNull().buffer().flowOn(dispatchers.io).map {
            widgetSseMapper.mappingWidget(it.event, it.message)
        }
    }

    override fun isConnected(): Boolean {
        return isSseConnected
    }

    private fun getBaseSseUrl(): String {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            SSE_STAGING_URL
        } else {
            SSE_PRODUCTION_URL
        }
    }

    private fun getRequest(url: String): Request {
        val authorization = String.format(BEARER, userSession.accessToken)
        val xDevice = String.format(ANDROID_VERSION, GlobalConfig.VERSION_NAME)

        return Request.Builder().get().url(url).addHeader(HEADER_X_DEVICE, xDevice)
            .addHeader(HEADER_AUTHORIZATION, authorization).build()
    }

    private fun getSseEventListener(url: String): ServerSentEvent.Listener {
        return object : ServerSentEvent.Listener {

            override fun onOpen(sse: ServerSentEvent, response: Response) {
                printLog("onOpen")
            }

            override fun onMessage(
                sse: ServerSentEvent, id: String, event: String, message: String
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
                sse: ServerSentEvent, throwable: Throwable, response: Response?
            ): Boolean {
                printLog("onRetryError : ${throwable.message}")
                val shouldRetry = retryCount < MAX_RETRY_COUNT
                if (shouldRetry) {
                    retryCount++
                } else {
                    closeSse()
                    retryCount = Int.ZERO
                }
                return shouldRetry
            }

            override fun onClosed(sse: ServerSentEvent) {
                printLog("onClosed")
                isSseConnected = false
                retryCount = Int.ZERO
            }

            override fun onPreRetry(sse: ServerSentEvent, originalRequest: Request): Request {
                printLog("onPreRetry : $retryCount")
                return getRequest(url)
            }
        }
    }

    private fun initLogger() {
        SSELogger.getInstance(context).init(LOG_KEY)
    }

    private fun printLog(s: String) {
        SSELogger.getInstance(context).send(s)
    }
}
