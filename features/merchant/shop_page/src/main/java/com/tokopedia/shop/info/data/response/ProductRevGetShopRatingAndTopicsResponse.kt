package com.tokopedia.shop.info.data.response


import com.google.gson.annotations.SerializedName

data class ProductRevGetShopRatingAndTopicsResponse(
    @SerializedName("productrevGetShopRatingAndTopics")
    val productrevGetShopRatingAndTopics: ProductrevGetShopRatingAndTopics
) {
    data class ProductrevGetShopRatingAndTopics(
        @SerializedName("rating")
        val rating: Rating
    ) {
        data class Rating(
            @SerializedName("detail")
            val detail: List<Detail>,
            @SerializedName("positivePercentageFmt")
            val positivePercentageFmt: String,
            @SerializedName("ratingScore")
            val ratingScore: String,
            @SerializedName("totalRating")
            val totalRating: Int,
            @SerializedName("totalRatingFmt")
            val totalRatingFmt: String,
            @SerializedName("totalRatingTextAndImage")
            val totalRatingTextAndImage: Int,
            @SerializedName("totalRatingTextAndImageFmt")
            val totalRatingTextAndImageFmt: String
        ) {
            data class Detail(
                @SerializedName("formattedTotalReviews")
                val formattedTotalReviews: String,
                @SerializedName("percentageFloat")
                val percentageFloat: Double,
                @SerializedName("rate")
                val rate: Int,
                @SerializedName("totalReviews")
                val totalReviews: Int
            )
        }
    }
}
