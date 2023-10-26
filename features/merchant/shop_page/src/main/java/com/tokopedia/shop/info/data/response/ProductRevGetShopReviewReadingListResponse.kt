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
            val avatar: String
        ) {
            data class LikeDislike(
                @SerializedName("likeStatus")
                val likeStatus: Int,
                @SerializedName("totalLike")
                val totalLike: Int
            )
        }
    }
}
