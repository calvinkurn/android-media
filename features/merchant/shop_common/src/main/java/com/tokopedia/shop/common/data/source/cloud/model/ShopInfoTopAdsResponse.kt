package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoTopAdsCategory.AUTO_ADS
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoTopAdsCategory.MANUAL_ADS

data class ShopInfoTopAdsResponse(
    @SerializedName("topAdsGetShopInfo")
    val response: ShopInfoTopAds = ShopInfoTopAds()
) {
    private companion object {
        val adsCategory = listOf(MANUAL_ADS, AUTO_ADS)
    }

    data class ShopInfoTopAds(
        @SerializedName("data")
        val data: Data = Data()
    )

    data class Data(
        @SerializedName("category")
        val category: Int = MANUAL_ADS,
        @SerializedName("category_desc")
        val categoryDesc: String = ""
    )

    fun isTopAds(): Boolean {
        val category = response.data.category
        return adsCategory.contains(category)
    }

    fun isAutoAds(): Boolean {
        val category = response.data.category
        return category == AUTO_ADS
    }
}