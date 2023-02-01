package com.tokopedia.play.broadcaster.ui.model.config

data class BroadcastingConfigUiModel(
    val authorID: String = "",
    val authorType: Int = 0,
    val config: Config = Config(),
) {
    data class Config(
        val audioRate: String = "",
        val bitrateMode: String = "",
        val fps: String = "",
        val maxRetry: Int = 0,
        val reconnectDelay: Int = 0,
        val videoBitrate: String = "",
        val videoHeight: String = "",
        val videoWidth: String = "",
    )
}
