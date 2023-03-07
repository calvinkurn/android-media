package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

data class AutoAdsResponse(

        @field:SerializedName("topAdsGetAutoAdsV2")
        val topAdsGetAutoAds: TopAdsGetAutoAds = TopAdsGetAutoAds()
) {
    data class TopAdsGetAutoAds(

            @field:SerializedName("data")
            val data: Data = Data()
    ) {
        data class Data(

                @field:SerializedName("shopID")
                val shopId: String = "0",

                @field:SerializedName("statusDesc")
                val statusDesc: String = "",

                @field:SerializedName("dailyUsage")
                val dailyUsage: Int = 0,

                @field:SerializedName("dailyBudget")
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