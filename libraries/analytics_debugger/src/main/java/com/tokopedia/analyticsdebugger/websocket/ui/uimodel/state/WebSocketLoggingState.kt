package com.tokopedia.analyticsdebugger.websocket.ui.uimodel.state

import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */
data class WebSocketLoggingState(
    val webSocketLogPagination: WebSocketLogPagination,
    val loading: Boolean,
)

data class WebSocketLogPagination(
    val webSocketLoggingList: List<WebSocketLogUiModel> = mutableListOf(),
    val query: String = "",
    val page: Int = 0,
    val isReachMax: Boolean = false,
)