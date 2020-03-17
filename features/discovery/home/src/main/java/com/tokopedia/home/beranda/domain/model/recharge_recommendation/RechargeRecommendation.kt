package com.tokopedia.home.beranda.domain.model.recharge_recommendation


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeRecommendation(
        @Expose
        @SerializedName("UUID")
        val UUID: String = "",

        @Expose
        @SerializedName("recommendations")
        val recommendations: List<RechargeRecommendationData> = listOf()
) {
    data class Response(
            @SerializedName("rechargeRecommendation")
            val response: RechargeRecommendation? = RechargeRecommendation()
    )
}