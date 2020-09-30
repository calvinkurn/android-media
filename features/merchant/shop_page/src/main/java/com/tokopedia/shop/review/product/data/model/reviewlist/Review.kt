package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class Review {
    @SerializedName("review_id")
    @Expose
    var reviewId = 0
    @SerializedName("reputation_id")
    @Expose
    var reputationId = 0
    @SerializedName("review_message")
    @Expose
    var reviewMessage: String? = null
    @SerializedName("product_rating")
    @Expose
    var productRating = 0
    @SerializedName("review_create_time")
    @Expose
    var reviewCreateTime: ReviewCreateTime? = null
    @SerializedName("review_update_time")
    @Expose
    var reviewUpdateTime: ReviewUpdateTime? = null
    @SerializedName("review_image_attachment")
    @Expose
    var reviewImageAttachment: List<ReviewImageAttachment>? = null
    @SerializedName("review_anonymous")
    @Expose
    var reviewAnonymous = 0
    @SerializedName("review_response")
    @Expose
    var reviewResponse: ReviewResponse? = null
    @SerializedName("user")
    @Expose
    var user: User? = null
    var totalLike = INACTIVE_LIKE_DISLIKE
    var likeStatus = 0

    val reviewStar: Float
        get() = if (productRating > MAX_STAR) {
            (productRating / DIVIDER_MAX_STAR).toFloat()
        } else productRating.toFloat()

    companion object {
        const val INACTIVE_LIKE_DISLIKE = -1
        const val MAX_STAR = 5
        const val DIVIDER_MAX_STAR = 20
    }
}