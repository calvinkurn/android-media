package com.tokopedia.smartbills.data

import com.google.gson.annotations.SerializedName


data class RechargeCloseResponse (
    @SerializedName("rechargeDeclineAboveTheFoldRecommendation")
    val recommendationClose: RecommendationClose = RecommendationClose()
)

data class RecommendationClose(
    @SerializedName("IsError")
    val isError : Boolean = false,
    @SerializedName("Message")
    val message : String = "",
)