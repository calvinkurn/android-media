package com.tokopedia.play.fake

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketResponse
import kotlinx.coroutines.flow.*

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
class FakePlayWebSocket(
    private val dispatchers: CoroutineDispatchers,
): PlayWebSocket {

    private val gson: Gson = Gson()

    private val webSocketFlow: MutableSharedFlow<WebSocketAction?> = MutableSharedFlow(extraBufferCapacity = 100)
    private var isOpen = false

    override fun listenAsFlow(): Flow<WebSocketAction> = webSocketFlow.filterNotNull().buffer().flowOn(dispatchers.io)

    override fun send(message: String) {
    }

    override fun connect(url: String, channelId: String, gcToken: String, source: String) {
        isOpen = true
    }

    override fun close() {
        isOpen = false
    }

    fun fakeReceivedMessage(text: String) {
        webSocketFlow.tryEmit(WebSocketAction.NewMessage(gson.fromJson(text, WebSocketResponse::class.java)))
    }
}