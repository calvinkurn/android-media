package com.tokopedia.product.detail.data.model.topads


import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.constant.TopAdsShopCategoryTypeDef


data class TopAdsGetShopInfo(
        @SerializedName("data")
        val topAdsShopData: TopAdsShopData = TopAdsShopData()
) {
    data class Response(
            @SerializedName("topAdsGetShopInfo")
            val topAdsGetShopInfo: TopAdsGetShopInfo = TopAdsGetShopInfo()
    )
}

data class TopAdsShopData(
        @SerializedName("category")
        val category: Int = TopAdsShopCategoryTypeDef.NO_ADS,
        @SerializedName("category_desc")
        val categoryDesc: String = ""
)





