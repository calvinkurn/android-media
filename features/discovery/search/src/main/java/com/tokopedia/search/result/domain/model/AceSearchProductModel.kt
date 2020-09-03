package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchProduct

data class AceSearchProductModel(
        @SerializedName("ace_search_product_v4")
        @Expose
        val searchProduct: SearchProduct = SearchProduct()
)