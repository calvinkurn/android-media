package com.tokopedia.scp_rewards.celebration.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ScpRewardsCelebrationModel(
    @Expose
    @SerializedName("scpRewardsCelebrationPage")
  val scpRewardsCelebrationPage : RewardsGetMedaliCelebrationPage? = null
){
    data class RewardsGetMedaliCelebrationPage(
        @Expose
        @SerializedName("resultStatus")
       val resultStatus : ResultStatus = ResultStatus(),
        @Expose
        @SerializedName("celebrationPage")
       val celebrationPage : CelebrationPage? = null
    ){
        data class ResultStatus(
            @Expose
            @SerializedName("code")
            val code:String = "",
            @Expose
            @SerializedName("status")
            val status:String = ""
        )
        data class CelebrationPage (
            @Expose
            @SerializedName("isNeedAppUpdate")
            val isNeedAppUpdate: Boolean = false,
            @Expose
            @SerializedName("backgroundColor")
            val backgroundColor: String = "",
            @Expose
            @SerializedName("backgroundColorImageURL")
            val backgroundColorImageURL: String = "",
            @Expose
            @SerializedName("backgroundImageURL")
            val backgroundImageURL: String = "",
            @Expose
            @SerializedName("title")
            val title: String = "",
            @Expose
            @SerializedName("medaliIconImageURL")
            val medaliIconImageURL: String = "",
            @Expose
            @SerializedName("medaliEffectImageURL")
            val medaliEffectImageURL: String = "",
            @Expose
            @SerializedName("medaliSpotLightImageURL")
            val medaliSpotLightImageURL: String = "",
            @Expose
            @SerializedName("medaliConfettiLottieURL")
            val medaliConfettiLottieURL: String = "",
            @Expose
            @SerializedName("medaliConfettiImageURL")
            val medaliConfettiImageURL: String = "",
            @Expose
            @SerializedName("medaliBlinkingLottieURL")
            val medaliBlinkingLottieURL: String = "",
            @Expose
            @SerializedName("medaliBlinkingImageURL")
            val medaliBlinkingImageURL: String = "",
            @Expose
            @SerializedName("medaliSoundEffectURL")
            val medaliSoundEffectURL: String = "",
            @Expose
            @SerializedName("medaliSourceText")
            val medaliSourceText: String = "",
            @Expose
            @SerializedName("medaliSourceFontColor")
            val medaliSourceFontColor: String = "",
            @Expose
            @SerializedName("medaliSourceBackgroundColor")
            val medaliSourceBackgroundColor: String = "",
            @Expose
            @SerializedName("medaliName")
            val medaliName: String = "",
            @Expose
            @SerializedName("medaliDescription")
            val medaliDescription: String = "",
            @Expose
            @SerializedName("redirectDelayInMilliseconds")
            val redirectDelayInMilliseconds: Long = 0L,
            @Expose
            @SerializedName("redirectURL")
            val redirectURL: String = "",
            @Expose
            @SerializedName("redirectAppLink")
            val redirectAppLink: String = "",
            @Expose
            @SerializedName("redirectSourceName")
            val redirectSourceName: String = ""
        )
    }
}
