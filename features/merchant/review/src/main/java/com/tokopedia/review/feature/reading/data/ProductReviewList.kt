package com.tokopedia.review.feature.reading.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
    val hasNext: Boolean = false
)

data class ProductReview(
    @SerializedName("feedbackID")
    @Expose
    var feedbackID: String = "",
    @SerializedName("variantName")
    @Expose
    val variantName: String = "",
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
    var imageAttachments: List<ProductReviewAttachments> = listOf(),
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
)

data class ProductReviewAttachments(
    @SerializedName("imageThumbnailUrl")
    @Expose
    val imageThumbnailUrl: String = "",
    @SerializedName("imageUrl")
    @Expose
    val imageUrl: String = ""
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

data class UserReviewStats(
    @SerializedName("key")
    @Expose
    val key: String = "",
    @SerializedName("formatted")
    @Expose
    val formatted: String = "",
    @SerializedName("count")
    @Expose
    val count: Int = 0
)