package com.tokopedia.topads.edit.data.response

import com.google.gson.annotations.SerializedName

data class GroupInfoResponse(

        @field:SerializedName("topAdsGetPromoGroup")
        val topAdsGetPromoGroup: TopAdsGetPromoGroup? = TopAdsGetPromoGroup()
) {
    data class TopAdsGetPromoGroup(

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
                val priceBid: Int = 0

        )
    }
}
