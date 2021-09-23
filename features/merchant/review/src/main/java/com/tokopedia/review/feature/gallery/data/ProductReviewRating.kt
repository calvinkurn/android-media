package com.tokopedia.review.feature.gallery.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.review.feature.reading.data.ProductRating

data class ProductReviewRatingResponse(
    @SerializedName("productrevGetProductRating")
    @Expose
    val productRating: ProductRating = ProductRating()
)