package com.tokopedia.play.fake

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play_common.sse.PlayChannelSSE
import com.tokopedia.play_common.sse.model.SSEAction
import com.tokopedia.play_common.sse.model.SSECloseReason
import com.tokopedia.play_common.sse.model.SSEResponse
import kotlinx.coroutines.flow.*

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
class FakePlayChannelSSE(
    private val dispatchers: CoroutineDispatchers,
): PlayChannelSSE {

    private var sseFlow = MutableSharedFlow<SSEAction>(extraBufferCapacity = 100)

    private var isOpen = false

    override fun connect(channelId: String, pageSource: String, gcToken: String) {
        isOpen = true
    }

    override fun close() {
        isOpen = false
        sseFlow.tryEmit(SSEAction.Close(SSECloseReason.INTENDED))
    }

    override fun listen(): Flow<SSEAction> = sseFlow.filterNotNull().buffer().flowOn(dispatchers.io)

    fun fakeSendMessage(event: String, message: String) {
        sseFlow.tryEmit(SSEAction.Message(SSEResponse(event = event, message = message)))
    }

    fun isConnectionOpen(): Boolean = isOpen
}