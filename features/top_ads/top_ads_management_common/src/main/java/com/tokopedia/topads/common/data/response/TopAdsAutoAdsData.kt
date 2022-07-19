package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

/**
 * Author errysuprayogi on 15,May,2019
 */
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
