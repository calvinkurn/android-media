package com.tokopedia.smartbills.data

import com.google.gson.annotations.SerializedName

data class RechargeRecommendationResponse(
    @SerializedName("rechargeRecommendation")
    val response: RechargeRecommendation = RechargeRecommendation()
)

data class RechargeRecommendation(
    @SerializedName("UUID")
    val UUID: String = "",

    @SerializedName("Recommendations")
    val recommendations: List<RechargeRecommendationData> = listOf()
)

data class RechargeRecommendationData(
    @SerializedName("ContentID")
    val contentID: String = "",
    @SerializedName("MainText")
    val mainText: String = "",
    @SerializedName("SubText")
    val subText: String = "",
    @SerializedName("AppLink")
    val applink: String = "",
    @SerializedName("Link")
    val link: String = "",
    @SerializedName("IconURL")
    val iconURL: String = "",
    @SerializedName("Title")
    val title: String = "",
    @SerializedName("BackgroundColor")
    val backgroundColor: String = "",
    @SerializedName("ButtonText")
    val buttonText: String = ""
)