package com.tokopedia.shop.common.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DynamicFilterModel

data class ShopFilterProductCountResponse(
        @SerializedName("searchProduct")
        @Expose
        val searchProductCount: SearchProductCount = SearchProductCount()
) {
    data class SearchProductCount(
            @SerializedName("count_text")
            @Expose
            val countText: String = ""
    )
}
