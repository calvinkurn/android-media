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
        val tapTapConfig: TapTapConfig = TapTapConfig(),

        @SerializedName("quizConfig")
        val quizConfig: QuizConfig = QuizConfig(),
    )

    data class TapTapConfig(
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

    data class QuizConfig(
        /** TODO: gonna change default value after gql is ready on prod */
        @SerializedName("isActive")
        val isActive: Boolean = true,

        @SerializedName("maxTitleLength")
        val maxTitleLength: Int = 30,

        @SerializedName("maxChoicesCount")
        val maxChoicesCount: Int = 3,

        @SerializedName("minChoicesCount")
        val minChoicesCount: Int = 2,

        @SerializedName("maxRewardLength")
        val maxRewardLength: Int = 30,

        @SerializedName("maxChoiceLength")
        val maxChoiceLength: Int = 30,

        @SerializedName("quizDurationsInSeconds")
        val quizDurationsInSeconds: List<Int> = listOf(3, 5),
    )
}