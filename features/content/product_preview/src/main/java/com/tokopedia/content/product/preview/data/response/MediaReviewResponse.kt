package com.tokopedia.content.product.preview.data.response

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 05/12/23
 */
data class MediaReviewResponse(
    @SerializedName("productrevGetProductReviewList")
    val data: ProductRevGetReviewMedia = ProductRevGetReviewMedia(),
) {
    data class ProductRevGetReviewMedia(
        @SerializedName("list")
        val review: List<ReviewDetail> = emptyList(),

        @SerializedName("shop")
        val shop: Shop = Shop(),

        @SerializedName("hasNext")
        val hasNext: Boolean = false,
    )

    data class Shop(
        @SerializedName("shopID")
        val shopId: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("image")
        val image: String = "",
    )

    data class ReviewDetail(
        @SerializedName("user")
        val user: ReviewerUserInfo = ReviewerUserInfo(),

        @SerializedName("feedbackID")
        val feedbackId: String = "",

        @SerializedName("variantName")
        val variantName: String = "",

        @SerializedName("productRating")
        val rating: Int = 0,

        @SerializedName("message")
        val review: String = "",

        @SerializedName("reviewCreateTimestamp")
        val createTimestamp: String = "",

        @SerializedName("isAnonymous")
        val isAnonymous: Boolean = false,

        @SerializedName("isReportable")
        val isReportable: Boolean = false,

        @SerializedName("userStats")
        val userStats: List<UserReviewStats> = emptyList(),

        @SerializedName("badRatingReasonFmt")
        val badRatingReasonFmt: String = "",

        @SerializedName("likeDislike")
        val likeStats: LikeStats = LikeStats(),

        @SerializedName("imageAttachments")
        val images: List<ReviewGalleryImage> = emptyList(),

        @SerializedName("videoAttachments")
        val videos: List<ReviewGalleryVideo> = emptyList(),
    )

    data class ReviewGalleryImage(
        @SerializedName("attachmentID")
        val attachmentId: String = "",

        @SerializedName("imageThumbnailUrl")
        val thumbnailUrl: String = "",

        @SerializedName("imageUrl")
        val fullSizeUrl: String = "",
    )

    data class ReviewGalleryVideo(
        @SerializedName("attachmentID")
        val attachmentId: String = "",
        @SerializedName("videoUrl")
        val url: String = "",
    )

    data class ReviewerUserInfo(
        @SerializedName("userID")
        val userId: String = "",

        @SerializedName("fullName")
        val fullName: String = "",

        @SerializedName("image")
        val image: String = "",

        @SerializedName("url")
        val url: String = "",

        @SerializedName("label")
        val label: String = "",
    )

    data class UserReviewStats(
        @SerializedName("key")
        val key: String = "",
        @SerializedName("formatted")
        val formatted: String = "",
        @SerializedName("count")
        val count: Int = 0
    )

    data class LikeStats(
        @SerializedName("totalLike")
        val totalLike: Int = 0,

        @SerializedName("likeStatus")
        val likeStatus: Int = 0,
    )
}
