package com.tokopedia.analyticsdebugger.websocket.ui.uimodel.event

import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.helper.UiString

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */
sealed class WebSocketLoggingEvent {
    data class ShowInfoEvent(val uiString: UiString): WebSocketLoggingEvent()
    object DeleteAllLogEvent: WebSocketLoggingEvent()
}