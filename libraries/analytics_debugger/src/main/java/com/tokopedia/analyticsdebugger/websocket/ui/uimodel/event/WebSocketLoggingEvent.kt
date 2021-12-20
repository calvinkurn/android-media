package com.tokopedia.analyticsdebugger.websocket.ui.uimodel.event

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */
sealed class WebSocketLoggingEvent {
    data class ShowInfoEvent(val message: String): WebSocketLoggingEvent()
}