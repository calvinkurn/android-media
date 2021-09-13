package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

class HeadlineInfoResponse(
    @field:SerializedName("topAdsGetPromoGroup")
    val topAdsGetPromoGroup: TopAdsGetPromoHeadline? = TopAdsGetPromoHeadline()
) {
    data class TopAdsGetPromoHeadline(

        @field:SerializedName("data")
        val data: Data = Data()
    ) {
        data class Data(

            @field:SerializedName("group_name")
            val groupName: String = "",

            @field:SerializedName("price_daily")
            val priceDaily: Int = 0,

            @field:SerializedName("shop_id")
            val shopId: String = "",

            @field:SerializedName("group_id")
            val groupId: String = "",

            @field:SerializedName("price_bid")
            val priceBid: Int = 0,

            @field:SerializedName("status")
            val status: String = "",

            @field:SerializedName("group_total")
            val groupTotal: String = "",

            @field:SerializedName("strategies")
            val strategies: List<String> = listOf()

        )
    }
}
