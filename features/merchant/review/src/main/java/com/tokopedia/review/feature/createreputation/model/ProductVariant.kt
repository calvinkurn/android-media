package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductVariant(
    @SerializedName("variantID")
    @Expose
    val variantId: String = "0",
    @SerializedName("variantName")
    @Expose
    val variantName: String = ""
) : Serializable