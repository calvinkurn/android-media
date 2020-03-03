package com.tokopedia.reviewseller.feature.reviewlist.data

import com.google.gson.annotations.SerializedName

data class ProductVariant(
        @SerializedName("variantID") val variantID : Int,
        @SerializedName("variantName") val variantName : String
)