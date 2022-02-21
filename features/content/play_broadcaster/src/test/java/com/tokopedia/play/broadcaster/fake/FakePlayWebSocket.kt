package com.tokopedia.play.broadcaster.fake

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketResponse
import kotlinx.coroutines.flow.*

/**
 * Created By : Jonathan Darwin on February 21, 2022
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

    override fun connect(channelId: String, gcToken: String, source: String) {
        isOpen = true
    }

    override fun close() {
        isOpen = false
    }

    fun fakeEmitMessage(text: String) {
        webSocketFlow.tryEmit(WebSocketAction.NewMessage(gson.fromJson(text, WebSocketResponse::class.java)))
    }

    fun isOpen() = isOpen
}