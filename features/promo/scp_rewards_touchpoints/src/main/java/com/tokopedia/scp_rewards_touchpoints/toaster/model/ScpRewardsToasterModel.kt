package com.tokopedia.scp_rewards_touchpoints.toaster.model

import com.google.gson.annotations.SerializedName

data class ScpRewardsToasterModel(
     @SerializedName("scpRewardsMedaliToasterTouchpointOrder")
     val scpRewardsToasterTouchpointOrder: ScpRewardsToasterTouchpointOrder? = null
){
    data class ScpRewardsToasterTouchpointOrder(
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus? = null,

        @SerializedName("medaliToasterTouchpointOrder")
        val medaliToasterTouchpointOrder: MedaliToasterTouchpointOrder? = null,

        @SerializedName("isShown")
        val isShown: Boolean? = null
    ) {
        data class ResultStatus(
            @SerializedName("code")
            val code: String? = null,

            @SerializedName("message")
            val message: List<String?>? = null,

            @SerializedName("status")
            val status: String? = null
        )
    }

    data class MedaliToasterTouchpointOrder(
        @SerializedName("cta")
        val cta: CtaItem? = null,

        @SerializedName("medaliIconImageURL")
        val medaliIconImageURL: String? = null,

        @SerializedName("medaliSlug")
        val medaliSlug: String? = null,

        @SerializedName("medaliID")
        val medaliID: Int? = null,

        @SerializedName("medaliSunburstImageURL")
        val medaliSunburstImageURL: String? = null,

        @SerializedName("infoMessage")
        val infoMessage: InfoMessage? = null
    ){
        data class CtaItem(
            @SerializedName("appLink")
            val appLink: String? = null,

            @SerializedName("isShown")
            val isShown: String? = null,

            @SerializedName("text")
            val text: String? = null,

            @SerializedName("url")
            val url: String? = null
        )

        data class InfoMessage(
            @SerializedName("subtitle")
            val subtitle: String? = null,

            @SerializedName("title")
            val title: String? = null
        )

    }

}
