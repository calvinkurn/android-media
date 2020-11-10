package com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListSeller

import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel

data class ShopShowcaseListSellerResponse(
        @SerializedName("shopShowcases")
        val shopShowcases: shopShowcases = shopShowcases()
)

data class shopShowcases(
        @SerializedName("error")
        val error: Error = Error(),
        @SerializedName("result")
        val result: List<ShopEtalaseModel> = listOf()
)

data class Error(
        @SerializedName("message")
        val message: String = ""
)
