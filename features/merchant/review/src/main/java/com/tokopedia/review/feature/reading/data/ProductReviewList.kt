package com.tokopedia.review.feature.reading.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.UserReviewStats

data class ProductReviewList(
    @SerializedName("productrevGetProductReviewList")
    @Expose
    val productrevGetProductReviewList: ProductrevGetProductReviewList = ProductrevGetProductReviewList()
)

data class ProductrevGetProductReviewList(
    @SerializedName("list")
    @Expose
    val reviewList: List<ProductReview> = listOf(),
    @SerializedName("shop")
    @Expose
    val shopInfo: ProductReviewShopInfo = ProductReviewShopInfo(),
    @SerializedName("hasNext")
    @Expose
    val hasNext: Boolean = false,
    @SerializedName("variantFilter")
    @Expose
    val variantFilter: VariantFilter = VariantFilter()
)

data class ProductReview(
    @SerializedName("feedbackID")
    @Expose
    var feedbackID: String = "",
    @SerializedName("variantName")
    @Expose
    var variantName: String = "",
    @SerializedName("message")
    @Expose
    var message: String = "",
    @SerializedName("productRating")
    @Expose
    var productRating: Int = 0,
    @SerializedName("reviewCreateTime")
    @Expose
    var reviewCreateTime: String = "",
    @SerializedName("reviewCreateTimestamp")
    @Expose
    var reviewCreateTimestamp: String = "",
    @SerializedName("isAnonymous")
    @Expose
    var isAnonymous: Boolean = false,
    @SerializedName("isReportable")
    @Expose
    var isReportable: Boolean = false,
    @SerializedName("reviewResponse")
    @Expose
    var reviewResponse: ProductReviewResponse = ProductReviewResponse(),
    @SerializedName("user")
    @Expose
    var user: ProductReviewUser = ProductReviewUser(),
    @SerializedName("imageAttachments")
    @Expose
    var imageAttachments: List<ProductReviewImageAttachments> = listOf(),
    @SerializedName("videoAttachments")
    @Expose
    var videoAttachments: List<ProductReviewVideoAttachments> = listOf(),
    @SerializedName("likeDislike")
    @Expose
    var likeDislike: LikeDislike = LikeDislike(),
    var shopProductId: String = "",
    @SerializedName("stats")
    @Expose
    var userReviewStats: List<UserReviewStats> = listOf(),
    @SerializedName("badRatingReasonFmt")
    @Expose
    var badRatingReasonFmt: String = ""
)

data class ProductReviewResponse(
    @SerializedName("message")
    @Expose
    var message: String = "",
    @SerializedName("createTime")
    @Expose
    var createTime: String = "",
)

data class ProductReviewUser(
    @SerializedName("userID")
    @Expose
    var userID: String = "",
    @SerializedName("fullName")
    @Expose
    var fullName: String = "",
    @SerializedName("image")
    @Expose
    val image: String = "",
    @SerializedName("url")
    @Expose
    val url: String = "",
    @SerializedName("label")
    @Expose
    val label: String = "",
)

data class ProductReviewImageAttachments(
    @SerializedName("attachmentID")
    @Expose
    val attachmentID: String = "",
    @SerializedName("imageThumbnailUrl")
    @Expose
    val imageThumbnailUrl: String = "",
    @SerializedName("imageUrl")
    @Expose
    val uri: String = ""
)

data class ProductReviewVideoAttachments(
    @SerializedName("attachmentID")
    @Expose
    val attachmentID: String = "",
    @SerializedName("videoUrl")
    @Expose
    val url: String = ""
)

data class ProductReviewShopInfo(
    @SerializedName("shopID")
    @Expose
    val shopID: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("url")
    @Expose
    val url: String = "",
    @SerializedName("image")
    @Expose
    val image: String = "",
)

data class LikeDislike(
    @SerializedName("totalLike")
    @Expose
    val totalLike: Int = 0,
    @SerializedName("likeStatus")
    @Expose
    val likeStatus: Int = 0
) {

    companion object {
        const val LIKED = 1
    }

    fun isLiked(): Boolean {
        return likeStatus == LIKED
    }
}

data class VariantFilter(
    @SerializedName("isUnavailable")
    @Expose
    val isUnavailable: Boolean = false,
    @SerializedName("ticker")
    @Expose
    val ticker: String = ""
)
