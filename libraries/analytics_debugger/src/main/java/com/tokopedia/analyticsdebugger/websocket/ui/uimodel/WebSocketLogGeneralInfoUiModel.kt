package com.tokopedia.analyticsdebugger.websocket.ui.uimodel

import java.io.Serializable

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
data class WebSocketLogGeneralInfoUiModel(
    val channelId: String = "",
    val gcToken: String = "",
): Serializable