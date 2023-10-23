package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AceSearchProductModelV5(
    @SerializedName("searchProductV5")
    @Expose
    val searchProduct: SearchProductV5 = SearchProductV5()
)
