package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewData(
    @SerializedName("review_id")
    @Expose
    var reviewId: Long = 0,

    @SerializedName("reputation_id")
    @Expose
    var reputationId: Long = 0,

    @SerializedName("review_title")
    @Expose
    var reviewTitle: String = "",

    @SerializedName("review_message")
    @Expose
    var reviewMessage: String = "",

    @SerializedName("review_rating")
    @Expose
    var reviewRating: Int = 0,

    @SerializedName("review_image_url")
    @Expose
    val reviewImageUrl: List<ReviewImageUrl> = listOf(),

    @SerializedName("review_create_time")
    @Expose
    var reviewCreateTime: ReviewCreateTime = ReviewCreateTime(),

    @SerializedName("review_update_time")
    @Expose
    var reviewUpdateTime: ReviewUpdateTime = ReviewUpdateTime(),

    @SerializedName("review_anonymity")
    @Expose
    var isReviewAnonymity: Boolean = false,

    @SerializedName("review_response")
    @Expose
    var reviewResponse: ReviewResponse = ReviewResponse()
)