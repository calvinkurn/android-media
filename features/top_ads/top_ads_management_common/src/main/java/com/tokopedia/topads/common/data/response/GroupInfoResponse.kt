package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

data class GroupInfoResponse(

        @field:SerializedName("topAdsGetPromoGroupProduct")
        val topAdsGetPromoGroup: TopAdsGetPromoGroup? = TopAdsGetPromoGroup()
) {
    data class TopAdsGetPromoGroup(

            @field:SerializedName("data")
            val data: Data = Data()
    ) {
        data class Data(

                @field:SerializedName("name")
                val groupName: String = "",

                @field:SerializedName("daily_budget")
                val daiyBudget: Int = 0,

                @field:SerializedName("shop_id")
                val shopId: String = "",

                @field:SerializedName("id")
                val groupId: String = "",

                @field:SerializedName("status")
                val status: String = "",

                @field:SerializedName("ad_total")
                val groupTotal: String = "",

                @field:SerializedName("bid_settings")
                val bidSettings: List<TopadsGroupBidSetting>? = listOf(),

                @field:SerializedName("strategies")
                val strategies: List<String> = listOf()

        )

        data class TopadsGroupBidSetting(
            @field:SerializedName("bid_type")
            var bidType: String? = null,

            @field:SerializedName("price_bid")
            var priceBid: Float? = 0.0f

        )

    }
}
