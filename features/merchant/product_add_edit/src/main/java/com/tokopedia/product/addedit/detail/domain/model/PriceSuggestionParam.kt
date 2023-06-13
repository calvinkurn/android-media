package com.tokopedia.product.addedit.detail.domain.model

import com.google.gson.annotations.SerializedName

data class PriceSuggestionParam(
        @SerializedName("keyword")
        var keyword: String = "",
        @SerializedName("categoryL3")
        var categoryL3: String = "",
        @SerializedName("size")
        var size: Int = 0
)