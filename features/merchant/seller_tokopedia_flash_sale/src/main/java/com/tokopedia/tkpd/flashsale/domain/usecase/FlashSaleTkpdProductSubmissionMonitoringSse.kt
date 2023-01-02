package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.authentication.HEADER_RELEASE_TRACK
import com.tokopedia.sse.OkSse
import com.tokopedia.sse.ServerSentEvent
import com.tokopedia.tkpd.flashsale.data.request.DoFlashSaleProductSubmissionRequest
import com.tokopedia.tkpd.flashsale.data.response.SSECloseReason
import com.tokopedia.tkpd.flashsale.data.response.SSEResponse
import com.tokopedia.tkpd.flashsale.data.response.SSEStatus
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.*
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject


class FlashSaleTkpdProductSubmissionMonitoringSse @Inject constructor(
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) {

    companion object {
        private const val URL_FLASH_SALE_TKPD_SSE =
            "campaign-participation/sellerpart/monitorsubmitproduct"
    }

    data class Param(
        val campaignId: Long,
        val productData: List<DoFlashSaleProductSubmissionRequest.ProductData>,
        val reservationId: String
    )

    private var sse: ServerSentEvent? = null
    private var flashSaleProductSubmissionResultFlow = MutableSharedFlow<SSEStatus>(extraBufferCapacity = 100)

    fun connect(sseKey: String) {
        val url = "${TokopediaUrl.getInstance().SSE}${URL_FLASH_SALE_TKPD_SSE}?sse_key=$sseKey"
        val request = Request.Builder().get().url(url)
            .header("Origin", TokopediaUrl.getInstance().WEB)
            .header("Accounts-Authorization", "Bearer ${userSession.accessToken}")
            .header("X-Device", "android-" + GlobalConfig.VERSION_NAME)
            .header(HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX)
            .build()
        sse = OkSse().newServerSentEvent(request, object : ServerSentEvent.Listener {
            override fun onOpen(sse: ServerSentEvent, response: Response) {
            }

            override fun onMessage(
                sse: ServerSentEvent,
                id: String,
                event: String,
                message: String
            ) {
                flashSaleProductSubmissionResultFlow.tryEmit(SSEStatus.Success(SSEResponse(event, message)))
            }

            override fun onComment(sse: ServerSentEvent, comment: String) {
            }

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
                flashSaleProductSubmissionResultFlow.tryEmit(SSEStatus.Close(SSECloseReason.INTENDED))
            }

            override fun onPreRetry(sse: ServerSentEvent, originalRequest: Request): Request {
                return request
            }
        })
    }

    fun closeSSE() {
        sse?.close()
    }

    fun listen(): Flow<SSEStatus> {
        return flashSaleProductSubmissionResultFlow.filterNotNull().buffer().flowOn(dispatchers.io)
    }
}
