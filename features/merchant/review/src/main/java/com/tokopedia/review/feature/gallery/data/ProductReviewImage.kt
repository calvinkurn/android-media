package com.tokopedia.review.feature.gallery.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.review.feature.reading.data.UserReviewStats

data class ProductrevGetReviewImageResponse(
    @SerializedName("productrevGetReviewImage")
    @Expose
    val productrevGetReviewImage: ProductrevGetReviewImage = ProductrevGetReviewImage()
)

data class ProductrevGetReviewImage(
    @SerializedName("list")
    @Expose
    val reviewImages: List<ReviewImage> = listOf(),
    @SerializedName("detail")
    @Expose
    val detail: Detail = Detail(),
    @SerializedName("hasNext")
    @Expose
    val hasNext: Boolean = false
)

data class ReviewImage(
    @SerializedName("imageID")
    @Expose
    val imageId: String = "",
    @SerializedName("feedbackID")
    @Expose
    val feedbackId: String = "",
    @SerializedName("ImageSiblings")
    @Expose
    val imageSiblings: List<String> = listOf(),
    @SerializedName("imageNumber")
    @Expose
    val imageNumber: Int = 0,
)

data class Detail(
    @SerializedName("review")
    @Expose
    val reviewDetail: List<ReviewDetail> = listOf(),
    @SerializedName("image")
    @Expose
    val reviewGalleryImages: List<ReviewGalleryImage> = listOf(),
    @SerializedName("imageCountFmt")
    @Expose
    val imageCountFmt: String = "",
    @SerializedName("imageCount")
    @Expose
    val imageCount: Long = 0L
)

data class ReviewDetail(
    @SerializedName("shopID")
    @Expose
    val shopId: String = "",
    @SerializedName("user")
    @Expose
    val user: ReviewerUserInfo = ReviewerUserInfo(),
    @SerializedName("feedbackID")
    @Expose
    val feedbackId: String = "",
    @SerializedName("variantName")
    @Expose
    val variantName: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("rating")
    @Expose
    val rating: Int = 0,
    @SerializedName("review")
    @Expose
    val review: String = "",
    @SerializedName("createTimestamp")
    @Expose
    val createTimestamp: String = "",
    @SerializedName("updateTimestamp")
    @Expose
    val updateTime: String = "",
    @SerializedName("isUpdated")
    @Expose
    val isUpdated: Boolean = false,
    @SerializedName("isReportable")
    @Expose
    val isReportable: Boolean = false,
    @SerializedName("isAnonymous")
    @Expose
    val isAnonymous: Boolean = false,
    @SerializedName("isLiked")
    @Expose
    val isLiked: Boolean = false,
    @SerializedName("totalLike")
    @Expose
    val totalLike: Int = 0,
    @SerializedName("userStats")
    @Expose
    val userStats: List<UserReviewStats> = listOf(),
    @SerializedName("badRatingReasonFmt")
    @Expose
    val badRatingReasonFmt: String = ""
)

data class ReviewGalleryImage(
    @SerializedName("attachmentID")
    @Expose
    val attachmentId: String = "",
    @SerializedName("thumbnailURL")
    @Expose
    val thumbnailURL: String = "",
    @SerializedName("fullsizeURL")
    @Expose
    val fullsizeURL: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("feedbackID")
    @Expose
    val feedbackId: String = "",
)

data class ReviewerUserInfo(
    @SerializedName("userID")
    @Expose
    val userId: String ="",
    @SerializedName("fullName")
    @Expose
    val fullName: String = "",
    @SerializedName("image")
    @Expose
    val image: String = "",
    @SerializedName("url")
    @Expose
    val url: String = ""
)
