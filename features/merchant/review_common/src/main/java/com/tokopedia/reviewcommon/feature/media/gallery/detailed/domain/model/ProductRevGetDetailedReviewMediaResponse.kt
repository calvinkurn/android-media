package com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductRevGetDetailedReviewMediaResponse(
    @SerializedName("productrevGetReviewImage") @Expose val productrevGetReviewMedia: ProductrevGetReviewMedia = ProductrevGetReviewMedia()
) : Serializable

data class ProductrevGetReviewMedia(
    @SerializedName("list") @Expose val reviewMedia: List<ReviewMedia> = listOf(),
    @SerializedName("detail") @Expose val detail: Detail = Detail(),
    @SerializedName("hasNext") @Expose val hasNext: Boolean = false,
    @SerializedName("hasPrev") @Expose val hasPrev: Boolean = false
) : Serializable

data class ReviewMedia(
    @SerializedName("imageID") @Expose val imageId: String = "0",
    @SerializedName("videoID") @Expose val videoId: String = "0",
    @SerializedName("feedbackID") @Expose val feedbackId: String = "",
    @SerializedName("ImageSiblings") @Expose val mediaSiblings: List<String> = listOf(),
    @SerializedName("imageNumber") @Expose val mediaNumber: Int = 0,
) : Serializable

data class Detail(
    @SerializedName("review") @Expose val reviewDetail: List<ReviewDetail> = listOf(),
    @SerializedName("image") @Expose val reviewGalleryImages: List<ReviewGalleryImage> = listOf(),
    @SerializedName("video") @Expose val reviewGalleryVideos: List<ReviewGalleryVideo> = listOf(),
    @SerializedName("imageCountFmt") @Expose val mediaCountFmt: String = "",
    @SerializedName("imageCount") @Expose val mediaCount: Long = 0L
) : Serializable

data class ReviewDetail(
    @SerializedName("shopID") @Expose val shopId: String = "",
    @SerializedName("user") @Expose val user: ReviewerUserInfo = ReviewerUserInfo(),
    @SerializedName("feedbackID") @Expose val feedbackId: String = "",
    @SerializedName("variantName") @Expose val variantName: String = "",
    @SerializedName("description") @Expose val description: String = "",
    @SerializedName("rating") @Expose val rating: Int = 0,
    @SerializedName("review") @Expose val review: String = "",
    @SerializedName("createTimestamp") @Expose val createTimestamp: String = "",
    @SerializedName("updateTimestamp") @Expose val updateTime: String = "",
    @SerializedName("isUpdated") @Expose val isUpdated: Boolean = false,
    @SerializedName("isReportable") @Expose val isReportable: Boolean = false,
    @SerializedName("isAnonymous") @Expose val isAnonymous: Boolean = false,
    @SerializedName("isLiked") @Expose val isLiked: Boolean = false,
    @SerializedName("totalLike") @Expose val totalLike: Int = 0,
    @SerializedName("userStats") @Expose val userStats: List<UserReviewStats> = listOf(),
    @SerializedName("badRatingReasonFmt") @Expose val badRatingReasonFmt: String = ""
) : Serializable

data class ReviewGalleryImage(
    @SerializedName("attachmentID") @Expose val attachmentId: String = "",
    @SerializedName("thumbnailURL") @Expose val thumbnailURL: String = "",
    @SerializedName("fullsizeURL") @Expose val fullsizeURL: String = "",
    @SerializedName("description") @Expose val description: String = "",
    @SerializedName("feedbackID") @Expose val feedbackId: String = "",
) : Serializable

data class ReviewGalleryVideo(
    @SerializedName("attachmentID") @Expose val attachmentId: String = "",
    @SerializedName("url") @Expose val url: String = "",
    @SerializedName("feedbackID") @Expose val feedbackId: String = "",
) : Serializable

data class ReviewerUserInfo(
    @SerializedName("userID") @Expose val userId: String = "",
    @SerializedName("fullName") @Expose val fullName: String = "",
    @SerializedName("image") @Expose val image: String = "",
    @SerializedName("url") @Expose val url: String = "",
    @SerializedName("label") @Expose val label: String = ""
) : Serializable

data class UserReviewStats(
    @SerializedName("key") @Expose val key: String = "",
    @SerializedName("formatted") @Expose val formatted: String = "",
    @SerializedName("count") @Expose val count: Int = 0
) : Serializable