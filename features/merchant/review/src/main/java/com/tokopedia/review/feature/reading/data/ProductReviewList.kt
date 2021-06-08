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
        val feedbackID: String = "",
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("productRating")
        @Expose
        val productRating: Int = 0,
        @SerializedName("reviewCreateTime")
        @Expose
        val reviewCreateTime: String = "",
        @SerializedName("isAnonymous")
        @Expose
        val isAnonymous: Boolean = false,
        @SerializedName("isReportable")
        @Expose
        val isReportable: Boolean = false,
        @SerializedName("reviewResponse")
        @Expose
        val reviewResponse: ProductReviewResponse = ProductReviewResponse(),
        @SerializedName("user")
        @Expose
        val user: ProductReviewUser = ProductReviewUser(),
        @SerializedName("imageAttachments")
        @Expose
        val imageAttachments: List<ProductReviewAttachments> = listOf()
)

data class ProductReviewResponse(
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("createTime")
        @Expose
        val createTime: String = "",
)

data class ProductReviewUser(
        @SerializedName("userID")
        @Expose
        val userID: String = "",
        @SerializedName("fullName")
        @Expose
        val fullName: String = "",
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