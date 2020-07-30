package com.tokopedia.tkpd.tkpdreputation.createreputation.model


import com.google.gson.annotations.SerializedName

data class ProductRevGetForm(
    @SerializedName("productrevGetForm")
    val productrevGetForm: ProductRevResponse = ProductRevResponse()
)