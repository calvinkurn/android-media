package com.tokopedia.review.feature.reviewlist.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductReviewListResponse(
        @SerializedName("productrevShopRatingAggregate")
    val productShopRatingAggregate: ProductShopRatingAggregate = ProductShopRatingAggregate()
) {
    data class ProductShopRatingAggregate(
            @SerializedName("data")
            val `data`: List<Data> = listOf(),
            @SerializedName("hasNext")
            val hasNext: Boolean = false
    ) {
        data class Data(
                @SerializedName("product")
                val product: Product = Product(),
                @SerializedName("rating")
                val rating: Float? = 0.0F,
                @SerializedName("reviewCount")
                val reviewCount: Int? = -1,
                @SerializedName("isKejarUlasan")
                @Expose
                val isKejarUlasan: Boolean = false

        ) {
            data class Product(
                    @SerializedName("productID")
                    val productID: Int? = -1,
                    @SerializedName("productImageURL")
                    val productImageURL: String? = "",
                    @SerializedName("productName")
                    val productName: String? = ""
            )
        }
    }
}