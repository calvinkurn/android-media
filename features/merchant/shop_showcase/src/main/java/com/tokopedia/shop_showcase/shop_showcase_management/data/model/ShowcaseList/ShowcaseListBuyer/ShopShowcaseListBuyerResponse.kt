package com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListBuyer

import com.google.gson.annotations.SerializedName
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseItem


data class ShopShowcaseListBuyerResponse(
        @SerializedName("shopShowcasesByShopID")
        val shopShowcasesByShopID: ShopShowcasesByShopID = ShopShowcasesByShopID()
)

data class ShopShowcasesByShopID(
        @SerializedName("error")
        val error: Error = Error(),
        @SerializedName("result")
        val result: List<ShowcaseItem> = listOf()
)

data class Error(
        @SerializedName("message")
        val message: String = ""
)
