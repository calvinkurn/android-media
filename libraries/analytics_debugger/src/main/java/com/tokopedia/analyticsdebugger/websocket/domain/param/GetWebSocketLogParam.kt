package com.tokopedia.analyticsdebugger.websocket.domain.param

import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.PageSource

data class GetWebSocketLogParam(
    val pageSource: PageSource,
    val query: String = "",
    val source: String = "",
    val page: Int = 0,
    val limit: Int = 0
)
