package com.tokopedia.analyticsdebugger.debugger.ws

import android.content.Context
import com.tokopedia.analyticsdebugger.debugger.WebSocketLogger

class PlayWebSocketLogger(context: Context) : WebSocketLogger {
    override fun init(data: String) {}
    override fun send(event: String) {}
    override fun send(event: String, message: String) {}
}
