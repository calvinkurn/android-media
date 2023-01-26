package com.tokopedia.analyticsdebugger.websocket.ui.uimodel

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */

// Play
data class PlayWebSocketLogGeneralInfoUiModel(
    val source: String = "",
    val channelId: String = "",
    val warehouseId: String = "",
    val gcToken: String = "",
)

// Topchat
data class TopchatWebSocketLogDetailInfoUiModel(
    val source: String = "",
    val url: String = ""
)
