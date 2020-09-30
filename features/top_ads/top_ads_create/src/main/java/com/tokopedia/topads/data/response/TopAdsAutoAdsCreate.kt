package com.tokopedia.topads.data.response

import com.google.gson.annotations.SerializedName

data class TopAdsAutoAdsCreate(

        @SerializedName("data")
        val data: Response.TopAdsAutoAdsData = Response.TopAdsAutoAdsData(),
        @SerializedName("errors")
        val error: List<ErrorResponse> = listOf(ErrorResponse())

) {
    data class Response(
            @SerializedName(value = "topAdsGetAutoAds", alternate = ["topAdsPostAutoAds"])
            val autoAds: TopAdsAutoAdsCreate
    ) {
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
    }
}
