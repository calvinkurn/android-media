package com.tokopedia.review.feature.createreputation.model


import com.google.gson.annotations.SerializedName

data class ProductRevGetForm(
    @SerializedName("productrevGetForm")
    val productrevGetForm: ProductRevResponse = ProductRevResponse()
)