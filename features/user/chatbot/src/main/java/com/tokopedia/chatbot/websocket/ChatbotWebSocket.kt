package com.tokopedia.chatbot.websocket

import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import okhttp3.Interceptor

interface ChatbotWebSocket {

    fun getDataFromSocketAsFlow(): Flow<ChatbotWebSocketAction>

    fun send(message: JsonObject?, interceptors: List<Interceptor>?)

    fun connect(url: String)

    fun close()
}
