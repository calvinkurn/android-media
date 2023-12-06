package com.tokopedia.content.product.preview.data

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 05/12/23
 */
data class MediaReviewResponse(
    @SerializedName("productrevGetReviewImage")
    val data: ProductRevGetReviewMedia = ProductRevGetReviewMedia(),
) {
    data class ProductRevGetReviewMedia(
        @SerializedName("list")
        val reviewMedia: List<ReviewMedia> = emptyList(),
        @SerializedName("")
        val detail: Detail = Detail(),
        @SerializedName("hasNext")
        val hasNext: Boolean = false,
        @SerializedName("hasPrev")
        val hasPrev: Boolean = false,
    )

    data class ReviewMedia(
        @SerializedName("imageID")
        val imageId: String = "",
        @SerializedName("videoID")
        val videoId: String = "",
        @SerializedName("feedbackID")
        val feedbackId: String = "",
        @SerializedName("ImageSiblings")
        val mediaSiblings: List<String> = emptyList(),
        @SerializedName("imageNumber")
        val mediaNumber: Int = 0,
    )

    data class Detail(
        @SerializedName("")
        val reviewDetail: List<ReviewDetail> = emptyList(),
        @SerializedName("image")
        val reviewGalleryImages: List<ReviewGalleryImage> = emptyList(),
        @SerializedName("video")
        val reviewGalleryVideos: List<ReviewGalleryVideo> = emptyList(),
        @SerializedName("imageCountFmt")
        val mediaCountFmt: String = "",
        @SerializedName("imageCount")
        val mediaCount: Long = 0L,
    )

    data class ReviewDetail(
        @SerializedName("shopID")
        val shopId: String = "",
        @SerializedName("user")
        val user: ReviewerUserInfo = ReviewerUserInfo(),
        @SerializedName("feedbackID")
        val feedbackId: String = "",
        @SerializedName("variantName")
        val variantName: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("rating")
        val rating: Int = 0,
        @SerializedName("")
        val review: String = "",
        @SerializedName("createTimestamp")
        val createTimestamp: String = "",
        @SerializedName("updateTimestamp")
        val updateTime: String = "",
        @SerializedName("isUpdated")
        val isUpdated: Boolean = false,
        @SerializedName("isReportable")
        val isReportable: Boolean = false,
        @SerializedName("isAnonymous")
        val isAnonymous: Boolean = false,
        @SerializedName("isLiked")
        val isLiked: Boolean = false,
        @SerializedName("totalLike")
        val totalLike: Int = 0,
        @SerializedName("userStats")
        val userStats: List<UserReviewStats> = emptyList(),
        @SerializedName("badRatingReasonFmt")
        val badRatingReasonFmt: String = "",
    )

    data class ReviewGalleryImage(
        @SerializedName("attachmentID")
        val attachmentId: String = "",
        @SerializedName("thumbnailURL")
        val thumbnailUrl: String = "",
        @SerializedName("fullsizeURL")
        val fullSizeUrl: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("feedbackID")
        val feedbackId: String = "",
    )

    data class ReviewGalleryVideo(
        @SerializedName("attachmentID")
        val attachmentId: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("feedbackID")
        val feedbackId: String = "",
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
}
