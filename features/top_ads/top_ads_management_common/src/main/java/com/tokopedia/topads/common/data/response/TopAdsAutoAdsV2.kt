package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

data class TopAdsAutoAdsV2(
    @SerializedName("data")
    val data: TopAdsAutoAdsData = TopAdsAutoAdsData(),
    @SerializedName("errors")
    val error: List<Error> = listOf(),
) {
    data class Response(
        @SerializedName(value = "topAdsPostAutoAdsV2", alternate = ["topAdsGetAutoAds"])
        val autoAds: TopAdsAutoAdsV2,
    )

    data class TopAdsAutoAdsData(
        @SerializedName("shopID")
        val shopId: String? = "",
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("statusDesc")
        val statusDesc: String? = "",
        @SerializedName("dailyBudget")
        val dailyBudget: Int = 0,
        @SerializedName("dailyUsage")
        val dailyUsage: Int = 0,
        @SerializedName("info")
        val adsInfo: TopAdsAutoAdsInfo? = TopAdsAutoAdsInfo(),
    ) {
        data class TopAdsAutoAdsInfo(
            @SerializedName("reason")
            val reason: String? = "",
            @SerializedName("message")
            val message: String? = "",
        )
    }
}
