package com.tokopedia.shop.common.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort

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

