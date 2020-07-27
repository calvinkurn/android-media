package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by zulfikarrahman on 1/15/18.
 */
class DataResponseReviewShop : DataResponseReview() {
    @SerializedName("list")
    @Expose
    var list: List<ReviewShop>? = null

}