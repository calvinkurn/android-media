package com.tokopedia.analyticsdebugger.debugger

interface WebSocketLogger {
    fun init(data: String)
    fun send(event: String)
    fun send(event: String, message: String)
}
