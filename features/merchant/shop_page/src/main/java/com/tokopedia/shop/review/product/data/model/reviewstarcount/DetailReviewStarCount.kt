package com.tokopedia.shop.review.product.data.model.reviewstarcount

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by zulfikarrahman on 1/15/18.
 */
class DetailReviewStarCount {
    @SerializedName("rate")
    @Expose
    var rate = 0
    @SerializedName("total_review")
    @Expose
    var totalReview = 0
    @SerializedName("percentage")
    @Expose
    var percentage: String? = null

}