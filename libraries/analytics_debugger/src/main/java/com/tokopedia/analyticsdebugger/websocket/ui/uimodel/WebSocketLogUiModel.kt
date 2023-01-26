package com.tokopedia.analyticsdebugger.websocket.ui.uimodel


/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
data class WebSocketLogUiModel(
    val id: Long = 0,
    val playGeneralInfo: PlayWebSocketLogGeneralInfoUiModel? = null,
    val topchatDetailInfo: TopchatWebSocketLogDetailInfoUiModel? = null,
    val event: String,
    val message: String,
    val dateTime: String,
): WebSocketLog()
