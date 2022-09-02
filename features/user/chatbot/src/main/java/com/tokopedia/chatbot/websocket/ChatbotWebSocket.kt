package com.tokopedia.chatbot.websocket

import kotlinx.coroutines.flow.Flow
import okhttp3.Interceptor

interface ChatbotWebSocket {

    fun getDataFromSocketAsFlow(): Flow<ChatbotWebSocketAction>

    fun send(message: String,  interceptors: List<Interceptor>?)

    fun connect(url : String)

    fun close()
}