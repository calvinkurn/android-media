package com.tokopedia.analyticsdebugger.websocket.domain.param

import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogPageSource
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel

data class InsertWebSocketLogParam(
    val pageSource: WebSocketLogPageSource,
    val info: WebSocketLogUiModel
)
