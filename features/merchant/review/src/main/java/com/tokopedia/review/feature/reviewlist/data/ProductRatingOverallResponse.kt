package com.tokopedia.review.feature.reviewlist.data

import com.google.gson.annotations.SerializedName

data class ProductRatingOverallResponse(
    @SerializedName("productrevGetProductRatingOverallByShop")
    val getProductRatingOverallByShop: ProductGetProductRatingOverallByShop = ProductGetProductRatingOverallByShop()
) {
    data class ProductGetProductRatingOverallByShop(
            @SerializedName("filterBy")
            val filterBy: String? = "",
            @SerializedName("productCount")
            val productCount: Int? = -1,
            @SerializedName("rating")
            val rating: Float? = -0.0F,
            @SerializedName("reviewCount")
            val reviewCount: Int? = -1
    )
}