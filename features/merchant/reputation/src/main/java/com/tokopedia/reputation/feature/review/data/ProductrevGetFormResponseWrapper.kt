package com.tokopedia.reputation.feature.review.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetFormResponseWrapper(
        @SerializedName("productrevGetForm")
        @Expose
        val productrevGetFormResponse: ProductrevGetFormResponse = ProductrevGetFormResponse()
)