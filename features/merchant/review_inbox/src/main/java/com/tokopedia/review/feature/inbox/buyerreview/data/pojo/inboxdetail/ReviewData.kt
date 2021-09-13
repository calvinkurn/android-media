package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewData {
    @SerializedName("review_id")
    @Expose
    var reviewId = 0

    @SerializedName("reputation_id")
    @Expose
    var reputationId = 0

    @SerializedName("review_title")
    @Expose
    var reviewTitle: String? = null

    @SerializedName("review_message")
    @Expose
    var reviewMessage: String? = null

    @SerializedName("review_rating")
    @Expose
    var reviewRating = 0

    @SerializedName("review_image_url")
    @Expose
    val reviewImageUrl: List<ReviewImageUrl>? = null

    @SerializedName("review_create_time")
    @Expose
    var reviewCreateTime: ReviewCreateTime? = null

    @SerializedName("review_update_time")
    @Expose
    var reviewUpdateTime: ReviewUpdateTime? = null

    @SerializedName("review_anonymity")
    @Expose
    var isReviewAnonymity = false

    @SerializedName("review_response")
    @Expose
    var reviewResponse: ReviewResponse? = null
}