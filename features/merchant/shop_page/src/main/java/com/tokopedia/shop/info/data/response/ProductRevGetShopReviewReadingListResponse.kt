package com.tokopedia.shop.info.data.response


import com.google.gson.annotations.SerializedName

data class ProductRevGetShopReviewReadingListResponse(
    @SerializedName("productrevGetShopReviewReadingList")
    val productrevGetShopReviewReadingList: ProductRevGetShopReviewReadingList
) {
    data class ProductRevGetShopReviewReadingList(
        @SerializedName("list")
        val list: List<ShopReviewList>,
        @SerializedName("shopName")
        val shopName: String
    ) {
        data class ShopReviewList(
            @SerializedName("badRatingReasonFmt")
            val badRatingReasonFmt: String,
            @SerializedName("likeDislike")
            val likeDislike: LikeDislike,
            @SerializedName("product")
            val product: Product,
            @SerializedName("rating")
            val rating: Int,
            @SerializedName("replyText")
            val replyText: String,
            @SerializedName("replyTime")
            val replyTime: String,
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
            @SerializedName("state")
            val state: State
        ) {
            data class LikeDislike(
                @SerializedName("likeStatus")
                val likeStatus: Int,
                @SerializedName("totalLike")
                val totalLike: Int
            )

            data class Product(
                @SerializedName("isDeletedProduct")
                val isDeletedProduct: Boolean,
                @SerializedName("productID")
                val productID: String,
                @SerializedName("productImageURL")
                val productImageURL: String,
                @SerializedName("productName")
                val productName: String,
                @SerializedName("productVariant")
                val productVariant: ProductVariant
            ) {
                data class ProductVariant(
                    @SerializedName("variantID")
                    val variantID: String,
                    @SerializedName("variantName")
                    val variantName: String
                )
            }

            data class State(
                @SerializedName("isAnonymous")
                val isAnonymous: Boolean,
                @SerializedName("isAutoReply")
                val isAutoReply: Boolean,
                @SerializedName("isReportable")
                val isReportable: Boolean
            )
        }
    }
}
