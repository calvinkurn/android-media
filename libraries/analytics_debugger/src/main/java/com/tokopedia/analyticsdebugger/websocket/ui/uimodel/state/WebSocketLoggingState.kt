package com.tokopedia.analyticsdebugger.websocket.ui.uimodel.state

import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLog
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketSourceUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.view.ChipModel

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */
data class WebSocketLoggingState(
    val webSocketLogPagination: WebSocketLogPagination,
    val loading: Boolean,
    val chips: List<ChipModel>,
)

data class WebSocketLogPagination(
    val webSocketLoggingList: List<WebSocketLog> = mutableListOf(),
    val query: String = "",
    val page: Int = 0,
    val isReachMax: Boolean = false,
)