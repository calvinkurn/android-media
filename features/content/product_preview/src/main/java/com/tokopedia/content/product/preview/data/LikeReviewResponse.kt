package com.tokopedia.content.product.preview.data

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 30/11/23
 */
data class LikeReviewResponse(
    @SerializedName("productrevLikeReview")
    val data: Data = Data(),
) {
    data class Data(
        @SerializedName("feedbackID")
        val reviewId: String = "",
        @SerializedName("likeStatus")
        val likeStatus: Int = 0,
        @SerializedName("totalLike")
        val totalLike: Int = 0,
        @SerializedName("totalDislike")
        val totalDislike: Int = 0,
    )
}
