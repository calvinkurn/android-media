package com.tokopedia.reviewseller.feature.reviewlist.data

import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("productID") val productID : Int,
        @SerializedName("productName") val productName : String,
        @SerializedName("productImageURL") val productImageURL : String,
        @SerializedName("productPageURL") val productPageURL : String,
        @SerializedName("productStatus") val productStatus : Int,
        @SerializedName("productVariant") val productVariant : ProductVariant
)