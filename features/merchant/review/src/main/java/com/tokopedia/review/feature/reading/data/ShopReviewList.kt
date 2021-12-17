package com.tokopedia.review.feature.reading.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopReviewList(
        @SerializedName("productrevGetShopReviewReadingList")
        @Expose
        val productrevGetShopReviewList: ProductrevGetShopReviewList = ProductrevGetShopReviewList()
)

data class ProductrevGetShopReviewList(
        @SerializedName("list")
        @Expose
        val shopReviewList: List<ShopReview> = listOf(),
        @SerializedName("shopName")
        @Expose
        val shopName: String = "",
        @SerializedName("hasNext")
        @Expose
        val hasNext: Boolean = false
)

data class ShopReview(
        @SerializedName("reviewID")
        @Expose
        val reviewID: String = "",
        @SerializedName("product")
        @Expose
        val product: Product = Product(),
        @SerializedName("rating")
        @Expose
        val rating: Int = 0,
        @SerializedName("reviewTime")
        @Expose
        val reviewTime: String = "",
        @SerializedName("reviewText")
        @Expose
        val reviewText: String = "",
        @SerializedName("reviewerID")
        @Expose
        val reviewerID: String = "",
        @SerializedName("reviewerName")
        @Expose
        val reviewerName: String = "",
        @SerializedName("replyText")
        @Expose
        val replyText: String = "",
        @SerializedName("replyTime")
        @Expose
        val replyTime: String = "",
        @SerializedName("attachments")
        @Expose
        val attachments: List<Attachments> = listOf(),
        @SerializedName("state")
        @Expose
        val state: State = State(),
        @SerializedName("likeDislike")
        @Expose
        var likeDislike: LikeDislike = LikeDislike(),
        @SerializedName("badRatingReasonFmt")
        @Expose
        var badRatingReasonFmt: String = ""
)

data class Product (
        @SerializedName("productID")
        @Expose
        val productID : String = "",
        @SerializedName("productName")
        @Expose
        val productName : String = "",
        @SerializedName("productImageURL")
        @Expose
        val productImageURL : String = "",
        @SerializedName("isDeletedProduct")
        @Expose
        val isDeletedProduct : Boolean = false,
        @SerializedName("productVariant")
        @Expose
        val productVariant : ProductVariant = ProductVariant()
)

data class ProductVariant (
        @SerializedName("variantID")
        @Expose
        val variantID : String = "",
        @SerializedName("variantName")
        @Expose
        val variantName : String = ""
)

data class Attachments (
        @SerializedName("thumbnailURL")
        @Expose
        val thumbnailURL : String = "",
        @SerializedName("fullsizeURL")
        @Expose
        val fullsizeURL : String = ""
)

data class State (
        @SerializedName("isReportable")
        @Expose
        val isReportable : Boolean = false,
        @SerializedName("isAutoReply")
        @Expose
        val isAutoReply : Boolean = false,
        @SerializedName("isAnonymous")
        @Expose
        val isAnonymous : Boolean = false
)