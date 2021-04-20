package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataResponseReviewProduct : DataResponseReview() {
    @SerializedName("product")
    @Expose
    var product: Product? = null
    @SerializedName("list")
    @Expose
    var list: List<Review>? = null

}