package com.tokopedia.attachproduct.data.model

import com.google.gson.annotations.SerializedName

data class AceSearchProductResponse(
    @SerializedName("ace_search_product")
    val aceSearchProductResponse: AceResponseWrapper = AceResponseWrapper()
)
