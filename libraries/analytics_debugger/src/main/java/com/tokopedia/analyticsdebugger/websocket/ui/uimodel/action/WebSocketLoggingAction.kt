package com.tokopedia.analyticsdebugger.websocket.ui.uimodel.action

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */
sealed class WebSocketLoggingAction {
    object InitPage: WebSocketLoggingAction()
    data class SearchLogAction(val query: String): WebSocketLoggingAction()
    object LoadNextPageAction: WebSocketLoggingAction()
    object DeleteAllLogAction: WebSocketLoggingAction()
    data class SelectSource(val value: String, val query: String): WebSocketLoggingAction()
}