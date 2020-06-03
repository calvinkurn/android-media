package com.tokopedia.review.feature.reviewform.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetFormProductVariant(
        @SerializedName("variantName")
        @Expose
        val variantName: String = ""
)