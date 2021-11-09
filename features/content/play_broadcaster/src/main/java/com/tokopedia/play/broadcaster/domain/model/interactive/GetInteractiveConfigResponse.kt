package com.tokopedia.play.broadcaster.domain.model.interactive

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 06/07/21.
 */
data class GetInteractiveConfigResponse(
    @SerializedName("playInteractiveGetInteractiveConfig")
    val interactiveConfig: InteractiveConfig = InteractiveConfig()
) {

    data class InteractiveConfig(
        @SerializedName("interactiveConfig")
        val config: Config = Config()
    )

    data class Config(
        @SerializedName("isActive")
        val isActive: Boolean = false,

        @SerializedName("interactiveNamingGuidelineHeader")
        val interactiveNamingGuidelineHeader: String = "",

        @SerializedName("interactiveNamingGuidelineDetail")
        val interactiveNamingGuidelineDetail: String = "",

        @SerializedName("interactiveTimeGuidelineHeader")
        val interactiveTimeGuidelineHeader: String = "",

        @SerializedName("interactiveTimeGuidelineDetail")
        val interactiveTimeGuidelineDetail: String = "",

        @SerializedName("interactiveDuration")
        val interactiveDuration: Int = 0,

        @SerializedName("countdownPickerTime")
        val countdownPickerTime: List<Int> = emptyList()
    )
}