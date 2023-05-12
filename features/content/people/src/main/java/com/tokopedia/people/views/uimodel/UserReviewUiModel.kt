package com.tokopedia.people.views.uimodel

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 12, 2023
 */
data class UserReviewUiModel(
    val reviewList: List<Review>,
    val page: Int,
    val hasNext: Boolean,
) {
    data class Review(
        val feedbackID: String,
        val product: Product,
        val rating: Int,
        val reviewText: String,
        val reviewTime: String,
        val attachments: List<Attachment>,
        val videoAttachments: List<VideoAttachment>,
        val likeDislike: LikeDislike
    )

    data class Product(
        val productID: String,
        val productName: String,
        val productImageURL: String,
        val productPageURL: String,
        val productStatus: Int,
    )

    data class Attachment(
        val attachmentID: String,
        val thumbnailURL: String,
        val fullsizeURL: String,
    )

    data class VideoAttachment(
        val attachmentID: String,
        val videoUrl: String,
    )

    data class LikeDislike(
        val totalLike: Int,
        val likeStatus: Int,
    )
}
