package com.tokopedia.topchat.common.websocket

interface WebSocketStateHandler {
    suspend fun scheduleForRetry(onStartRetry: suspend () -> Unit)
    fun retrySucceed()
}