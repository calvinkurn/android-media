package com.tokopedia.tokopedianow.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetReferralReceiverHomeResponse(
    @SerializedName("gamiReferralReceiverHome")
    @Expose
    val gamiReferralReceiverHome: GamiReferralReceiverHome
) {
    data class GamiReferralReceiverHome(
        @Expose
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus,
        @Expose
        @SerializedName("reward")
        val reward: Reward,

        @Expose
        @SerializedName("benefits")
        val benefits: List<Benefit> = listOf()
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

        data class Benefit(
            @SerializedName("description")
            val description: String = "",

            @SerializedName("benefitText")
            val benefitText: String = "",

            @SerializedName("title")
            val title: String = ""
        )
    }
}
