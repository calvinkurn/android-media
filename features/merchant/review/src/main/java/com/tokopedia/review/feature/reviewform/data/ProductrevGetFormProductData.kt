package com.tokopedia.review.feature.reviewform.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetFormProductData(
        @SerializedName("productID")
        @Expose
        val productId: Int = 0,
        @SerializedName("productName")
        @Expose
        val productName: String = "",
        @SerializedName("productImageURL")
        @Expose
        val productImageUrl: String = "",
        @SerializedName("productPageURL")
        @Expose
        val productPageUrl: String = "",
        @SerializedName("productVariant")
        @Expose
        val productVariant: ProductrevGetFormProductVariant = ProductrevGetFormProductVariant()
)