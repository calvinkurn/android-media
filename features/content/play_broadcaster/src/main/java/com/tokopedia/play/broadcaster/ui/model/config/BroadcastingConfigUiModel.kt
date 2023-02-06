package com.tokopedia.play.broadcaster.ui.model.config

data class BroadcastingConfigUiModel(
    val audioRate: String = "",
    val bitrateMode: String = "",
    val fps: String = "",
    val maxRetry: Int = 0,
    val reconnectDelay: Int = 0,
    val videoBitrate: String = "",
    val videoHeight: String = "",
    val videoWidth: String = "",
)
