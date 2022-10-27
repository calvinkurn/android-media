package com.tokopedia.tokopedianow.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetReferralSenderHomeResponse(
    @SerializedName("gamiReferralSenderHome")
    @Expose
    val gamiReferralSenderHome: GamiReferralSenderHome
) {
    data class GamiReferralSenderHome(
        @Expose
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus,
        @SerializedName("reward")
        val reward: Reward,
        @Expose
        @SerializedName("sharingMetadata")
        val sharingMetaData: SharingMetadata,
        @Expose
        @SerializedName("actionButton")
        val actionButton: ActionButton
    ) {
        data class ResultStatus(
            @Expose
            @SerializedName("code")
            val code: String,
            @Expose
            @SerializedName("message")
            val message: List<String>,
            @Expose
            @SerializedName("reason")
            val reason: String
        )
        data class Reward(
            @SerializedName("maxReward")
            val maxReward: String
        )
        data class SharingMetadata(
            @Expose
            @SerializedName("ogImage")
            val ogImage: String,
            @Expose
            @SerializedName("ogTitle")
            val ogTitle: String,
            @Expose
            @SerializedName("ogDescription")
            val ogDescription: String,
            @Expose
            @SerializedName("textDescription")
            val textDescription: String,
            @Expose
            @SerializedName("sharingURL")
            val sharingUrl: String
        )
        data class ActionButton(
            @Expose
            @SerializedName("text")
            val text: String,
            @Expose
            @SerializedName("url")
            val url: String,
            @Expose
            @SerializedName("appLink")
            val appLink: String,
            @Expose
            @SerializedName("type")
            val type: String
        )
    }
}
