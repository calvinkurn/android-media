package com.tokopedia.shop.common.graphql.data.shopsort

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GqlShopSortProductResponse(
    @SerializedName("shopSort")
    @Expose
    val shopSort: ShopSort
) {
    data class ShopSort(
        @SerializedName("data")
        @Expose
        val data: Data
    ) {
        data class Data(
            @SerializedName("sort")
            @Expose
            val sort: List<ShopProductSort>
        )
    }
}
