package com.tokopedia.shop.review.product.data.model.reviewstarcount

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by zulfikarrahman on 1/15/18.
 */
class DataResponseReviewStarCount {
    @SerializedName("rating_score")
    @Expose
    var ratingScore: String? = null
    @SerializedName("total_review")
    @Expose
    var totalReview = 0
    @SerializedName("detail")
    @Expose
    var detail: List<DetailReviewStarCount>? = null

}