package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductVariant(
        @SerializedName("variantID")
        @Expose
        val variantId: Int = 0,
        @SerializedName("variantName")
        @Expose
        val variantName: String = ""
)