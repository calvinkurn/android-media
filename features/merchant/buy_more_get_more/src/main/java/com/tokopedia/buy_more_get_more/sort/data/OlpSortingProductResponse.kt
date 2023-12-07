package com.tokopedia.buy_more_get_more.sort.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.graphql.data.shopsort.ShopProductSort

data class OlpSortingProductResponse(
    @SerializedName("GetOfferingSortingList")
    @Expose
    val getOfferingSortingList: GetOfferingSortingList
) {
    data class GetOfferingSortingList(
        @SerializedName("data")
        @Expose
        val sort: List<ShopProductSort>
    )
}
