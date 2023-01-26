package com.tokopedia.analyticsdebugger.websocket.domain.param

import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel

data class InsertWebSocketLogParam(
    val event: String,
    val message: String,
    val info: WebSocketLogUiModel
)
