package com.tokopedia.home.beranda.domain.model.recharge_recommendation


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DeclineRechargeRecommendation(
        @Expose
        @SerializedName("isError")
        val isError: Boolean = false,

        @Expose
        @SerializedName("message")
        val message: String = ""
) {
    data class Response(
            @SerializedName("rechargeDeclineAboveTheFoldRecommendation")
            val response: DeclineRechargeRecommendation = DeclineRechargeRecommendation()
    )
}