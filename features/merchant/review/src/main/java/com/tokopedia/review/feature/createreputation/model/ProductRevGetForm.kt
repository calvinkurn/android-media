package com.tokopedia.review.feature.createreputation.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductRevGetForm(
    @SerializedName("productrevGetFormV2")
    @Expose
    val productrevGetForm: ProductRevResponse = ProductRevResponse()
)