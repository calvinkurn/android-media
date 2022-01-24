package com.tokopedia.review.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ToggleLikeReviewResponse(
        @SerializedName("productrevLikeReview")
        @Expose
        val productrevLikeReview: ProductrevLikeReview = ProductrevLikeReview()
)

data class ProductrevLikeReview(
    @SerializedName("feedbackID")
        @Expose
        val feedbackId: String = "",
    @SerializedName("likeStatus")
        @Expose
        val likeStatus: Int = 0,
    @SerializedName("totalLike")
        @Expose
        val totalLike: Int = 0,
    @SerializedName("totalDislike")
        @Expose
        val totalDislike: Int = 0
) {
    companion object {
        private const val LIKED = 1
    }

    fun isLiked(): Boolean {
        return likeStatus == LIKED
    }
}