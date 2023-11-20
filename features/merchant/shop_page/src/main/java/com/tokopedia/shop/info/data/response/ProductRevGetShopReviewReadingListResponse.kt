package com.tokopedia.shop.info.data.response

import com.google.gson.annotations.SerializedName

data class ProductRevGetShopReviewReadingListResponse(
    @SerializedName("productrevGetShopReviewReadingList")
    val productrevGetShopReviewReadingList: ProductRevGetShopReviewReadingList
) {
    data class ProductRevGetShopReviewReadingList(
        @SerializedName("list")
        val list: List<ShopReviewList>,
        @SerializedName("totalReviews")
        val totalReviews: Int
    ) {
        data class ShopReviewList(
            @SerializedName("likeDislike")
            val likeDislike: LikeDislike,
            @SerializedName("product")
            val product: Product,
            @SerializedName("attachments")
            val attachments: List<Attachment>,
            @SerializedName("rating")
            val rating: Int,
            @SerializedName("reviewID")
            val reviewID: String,
            @SerializedName("reviewText")
            val reviewText: String,
            @SerializedName("reviewTime")
            val reviewTime: String,
            @SerializedName("reviewerID")
            val reviewerID: String,
            @SerializedName("reviewerName")
            val reviewerName: String,
            @SerializedName("reviewerLabel")
            val reviewerLabel: String,
            @SerializedName("avatar")
            val avatar: String,
            @SerializedName("state")
            val state: State,
            @SerializedName("badRatingReasonFmt")
            val badRatingReasonFmt: String
        ) {
            data class LikeDislike(
                @SerializedName("likeStatus")
                val likeStatus: Int,
                @SerializedName("totalLike")
                val totalLike: Int
            )
            data class Attachment(
                @SerializedName("attachmentID")
                val attachmentId: String,
                @SerializedName("thumbnailURL")
                val thumbnailURL: String,
                @SerializedName("fullsizeURL")
                val fullSizeURL: String
            )

            data class Product(
                @SerializedName("productID")
                val productID: String,
                @SerializedName("productName")
                val productName: String,
                @SerializedName("productVariant")
                val productVariant: ProductVariant
            ) {
                data class ProductVariant(
                    @SerializedName("variantID")
                    val variantId: String,
                    @SerializedName("variantName")
                    val variantName: String
                )
            }
            data class State(
                @SerializedName("isReportable")
                val isReportable: Boolean,
                @SerializedName("isAutoReply")
                val isAutoReply: Boolean,
                @SerializedName("isAnonymous")
                val isAnonymous: Boolean
            )
        }
    }
}
