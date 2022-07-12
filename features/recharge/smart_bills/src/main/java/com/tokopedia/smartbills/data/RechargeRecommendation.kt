package com.tokopedia.smartbills.data

import com.google.gson.annotations.SerializedName

data class RechargeRecommendation(
    @SerializedName("UUID")
    val UUID: String = "",

    @SerializedName("recommendations")
    val recommendations: List<RechargeRecommendationData> = listOf()
) {
    data class Response(
        @SerializedName("rechargeRecommendation")
        val response: RechargeRecommendation? = RechargeRecommendation()
    )
}

data class RechargeRecommendationData(
    @SerializedName("contentID")
    val contentID: String = "",
    @SerializedName("mainText")
    val mainText: String = "",
    @SerializedName("subText")
    val subText: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("link")
    val link: String = "",
    @SerializedName("iconURL")
    val iconURL: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("backgroundColor")
    val backgroundColor: String = "",
    @SerializedName("buttonText")
    val buttonText: String = ""
)