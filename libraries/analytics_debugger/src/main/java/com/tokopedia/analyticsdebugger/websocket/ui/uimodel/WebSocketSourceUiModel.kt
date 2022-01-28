package com.tokopedia.analyticsdebugger.websocket.ui.uimodel

/**
 * Created By : Jonathan Darwin on December 21, 2021
 */
data class WebSocketSourceUiModel(
    val label: String,
    val value: String,
    var selected: Boolean = false,
)