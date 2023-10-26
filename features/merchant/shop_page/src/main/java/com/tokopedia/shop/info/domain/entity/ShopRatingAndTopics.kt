package com.tokopedia.shop.info.domain.entity

data class ShopRatingAndTopics(val rating: Rating) {
    data class Rating(
        val detail: List<Detail>,
        val positivePercentageFmt: String,
        val ratingScore: String,
        val totalRating: Int,
        val totalRatingFmt: String
    ) {
        data class Detail(
            val formattedTotalReviews: String,
            val percentageFloat: Double,
            val rate: Int,
            val totalReviews: Int
        )
    }
}

