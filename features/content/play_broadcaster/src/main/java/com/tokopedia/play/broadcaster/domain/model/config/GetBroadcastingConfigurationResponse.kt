package com.tokopedia.play.broadcaster.domain.model.config

import com.google.gson.annotations.SerializedName

data class GetBroadcastingConfigurationResponse(
    @SerializedName("broadcasterGetBroadcastingConfig")
    val broadcasterGetBroadcastingConfig: BroadcasterGetBroadcastingConfig = BroadcasterGetBroadcastingConfig()
) {
    data class BroadcasterGetBroadcastingConfig(
        @SerializedName("authorID")
        val authorID: String = "",
        @SerializedName("authorType")
        val authorType: Int = 0,
        @SerializedName("config")
        val config: Config = Config(),
    ) {
        data class Config(
            @SerializedName("audioRate")
            val audioRate: String = "",
            @SerializedName("bitrateMode")
            val bitrateMode: String = "",
            @SerializedName("fps")
            val fps: String = "",
            @SerializedName("maxRetry")
            val maxRetry: Int = 0,
            @SerializedName("reconnectDelay")
            val reconnectDelay: Int = 0,
            @SerializedName("videoBitrate")
            val videoBitrate: String = "",
            @SerializedName("videoHeight")
            val videoHeight: String = "",
            @SerializedName("videoWidth")
            val videoWidth: String = "",
        )
    }
}
