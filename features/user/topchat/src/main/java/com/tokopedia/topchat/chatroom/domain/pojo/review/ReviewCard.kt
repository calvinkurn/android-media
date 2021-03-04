package com.tokopedia.topchat.chatroom.domain.pojo.review

import com.google.gson.annotations.SerializedName

data class ReviewCard(
        @SerializedName("product_id")
        val productId: String = "0",
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("reputation_id")
        val reputationId: Long = 0,
        @SerializedName("buyer_id")
        val buyerId: Long = 0,
        @SerializedName("allow_review")
        val allowReview: Boolean = false,
        @SerializedName("is_reviewed")
        var isReviewed: Boolean = false,
        @SerializedName("rating")
        var rating: Float = 0f,
        @SerializedName("review_url")
        var reviewUrl: String = "",
        @SerializedName("feedback_id")
        val feedBackId: String = ""
)