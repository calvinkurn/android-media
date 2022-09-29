package com.tokopedia.chatbot.fake

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chatbot.websocket.ChatWebSocketResponse
import com.tokopedia.chatbot.websocket.ChatbotWebSocket
import com.tokopedia.chatbot.websocket.ChatbotWebSocketAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import okhttp3.Interceptor

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
class FakeChatbotWebSocket(
    private val dispatchers: CoroutineDispatchers
) : ChatbotWebSocket {

    private val gson: Gson = Gson()

    private val webSocketFlow: MutableSharedFlow<ChatbotWebSocketAction> = MutableSharedFlow(extraBufferCapacity = 100)
    private var isOpen = false

    override fun getDataFromSocketAsFlow(): Flow<ChatbotWebSocketAction> {
        return webSocketFlow.filterNotNull().buffer().flowOn(dispatchers.io)
    }

    override fun send(message: JsonObject?, interceptors: List<Interceptor>?) {
    }

    override fun connect(url: String) {
        isOpen = true
    }

    override fun close() {
        isOpen = false
    }

    fun fakeReceivedMessage(text: String) {
        webSocketFlow.tryEmit(ChatbotWebSocketAction.NewMessage(gson.fromJson(text, ChatWebSocketResponse::class.java)))
    }

    fun isOpen(): Boolean{
        return isOpen
    }

}
