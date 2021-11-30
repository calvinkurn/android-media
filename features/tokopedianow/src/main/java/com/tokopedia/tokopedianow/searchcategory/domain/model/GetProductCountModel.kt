package com.tokopedia.tokopedianow.searchcategory.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetProductCountModel {
    @SerializedName("searchProduct")
    @Expose
    val searchProductCount = SearchProductCount()

    class SearchProductCount {
        @SerializedName("count_text")
        @Expose
        val countText = ""
    }
}