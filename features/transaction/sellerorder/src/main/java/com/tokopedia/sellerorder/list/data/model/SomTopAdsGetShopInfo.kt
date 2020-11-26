package com.tokopedia.sellerorder.list.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomTopAdsGetShopInfoResponse(
        @SerializedName("data")
        @Expose
        val data: Data = Data()
) {
    data class Data(
            @SerializedName("topAdsGetShopInfo")
            @Expose
            val topAdsGetShopInfo: TopAdsGetShopInfo = TopAdsGetShopInfo()
    ) {
        data class TopAdsGetShopInfo(
                @SerializedName("data")
                @Expose
                val somTopAdsShopInfo: SomTopAdsShopInfo = SomTopAdsShopInfo()
        ) {
            data class SomTopAdsShopInfo(
                    @SerializedName("category")
                    @Expose
                    val category: Int = 1
            )
        }
    }
}