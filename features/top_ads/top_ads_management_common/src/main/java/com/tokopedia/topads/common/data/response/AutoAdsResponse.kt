package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

data class AutoAdsResponse(

        @field:SerializedName("topAdsGetAutoAds")
        val topAdsGetAutoAds: TopAdsGetAutoAds = TopAdsGetAutoAds()
) {
    data class TopAdsGetAutoAds(

            @field:SerializedName("data")
            val data: Data = Data()
    ) {
        data class Data(

                @field:SerializedName("shop_id")
                val shopId: Int = 0,

                @field:SerializedName("status_desc")
                val statusDesc: String = "",

                @field:SerializedName("daily_usage")
                val dailyUsage: Int = 0,

                @field:SerializedName("daily_budget")
                val dailyBudget: Int = 0,

                @field:SerializedName("status")
                val status: Int = 0,

                @field:SerializedName("info")
                val info: Info = Info()
        ) {
            data class Info(

                    @field:SerializedName("reason")
                    val reason: String = "",

                    @field:SerializedName("message")
                    val message: String = ""
            )
        }
    }
}