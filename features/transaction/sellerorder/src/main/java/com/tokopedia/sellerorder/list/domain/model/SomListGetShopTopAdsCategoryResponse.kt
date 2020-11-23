package com.tokopedia.sellerorder.list.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListGetShopTopAdsCategoryResponse(
        @SerializedName("data")
        @Expose
        val `data`: Data = Data()
) {
    data class Data(
            @SerializedName("topAdsGetShopInfo")
            @Expose
            val topAdsGetShopInfo: TopAdsGetShopInfo = TopAdsGetShopInfo()
    ) {
        data class TopAdsGetShopInfo(
                @SerializedName("data")
                @Expose
                val `data`: Data = Data()
        ) {
            data class Data(
                    @SerializedName("category")
                    @Expose
                    val category: Int = 0
            )
        }
    }
}