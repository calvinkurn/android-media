package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by zulfikarrahman on 1/15/18.
 */
class DataResponseReviewHelpful : DataResponseReview() {
    @SerializedName("list")
    @Expose
    var list: List<Review>? = null

}