package com.tokopedia.analyticsdebugger.websocket.ui.uimodel

import java.io.Serializable


/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
data class WebSocketLogUiModel(
    val id: Long = 0,
    val generalInfo: WebSocketLogGeneralInfoUiModel,
    val event: String,
    val message: String,
    val dateTime: String,
): WebSocketLog()