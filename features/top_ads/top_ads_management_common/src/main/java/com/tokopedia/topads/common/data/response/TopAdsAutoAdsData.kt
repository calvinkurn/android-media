package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

/**
 * Author errysuprayogi on 15,May,2019
 */
data class TopAdsAutoAdsData(

        @SerializedName("shop_id")
        val shopId: Int = 0,
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("status_desc")
        val statusDesc: String = "",
        @SerializedName("daily_budget")
        val dailyBudget: Int = 0,
        @SerializedName("daily_usage")
        val dailyUsage: Int = 0,
        @SerializedName("info")
        val adsInfo: TopAdsAutoAdsInfo = TopAdsAutoAdsInfo()
) {
    data class TopAdsAutoAdsInfo(

            @SerializedName("reason")
            val reason: String = "",
            @SerializedName("message")
            val message: String = ""
    )

}
