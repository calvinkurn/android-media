package com.tokopedia.shop.common.graphql.data.shopetalase

import com.google.gson.annotations.SerializedName


data class ShopShowcaseListResponse(
        @SerializedName("shopShowcasesByShopID")
        val shopShowcasesByShopID: ShopShowcasesByShopID = ShopShowcasesByShopID()
)

data class ShopShowcasesByShopID(
        @SerializedName("error")
        val error: Error = Error(),
        @SerializedName("result")
        val result: List<ShopEtalaseModel> = listOf()
)

data class Error(
        @SerializedName("message")
        val message: String = ""
)
