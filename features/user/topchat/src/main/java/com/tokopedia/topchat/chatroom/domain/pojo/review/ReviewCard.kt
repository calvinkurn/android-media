package com.tokopedia.topchat.chatroom.domain.pojo.review

import com.google.gson.annotations.SerializedName

data class ReviewCard(
        @SerializedName("product_id")
        val productId: Int = 0,
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("reputation_id")
        val reputationId: Int = 0,
        @SerializedName("buyer_id")
        val buyerId: Int = 0,
        @SerializedName("allow_review")
        val allowReview: Boolean = false,
        @SerializedName("is_reviewed")
        val isReviewed: Boolean = false,
        @SerializedName("rating")
        val rating: Float = 0f,
        @SerializedName("review_url")
        val reviewUrl: String = "",
        @SerializedName("feedback_id")
        val feedBackId: Int = 0
)