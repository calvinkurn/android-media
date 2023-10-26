package com.tokopedia.shop.info.data.mapper

import com.tokopedia.shop.info.data.response.ProductRevGetShopRatingAndTopicsResponse
import com.tokopedia.shop.info.domain.entity.ShopRatingAndTopics
import javax.inject.Inject

class ProductRevGetShopRatingAndTopicsMapper @Inject constructor() {
    fun map(response: ProductRevGetShopRatingAndTopicsResponse): ShopRatingAndTopics {
        val rating = response.productrevGetShopRatingAndTopics.rating

        return ShopRatingAndTopics(
            ShopRatingAndTopics.Rating(
                rating.toDetail(),
                rating.positivePercentageFmt,
                rating.ratingScore,
                rating.totalRating,
                rating.totalRatingFmt
            )
        )
    }

    private fun ProductRevGetShopRatingAndTopicsResponse.ProductrevGetShopRatingAndTopics.Rating.toDetail(): List<ShopRatingAndTopics.Rating.Detail> {
        return detail.map {
            ShopRatingAndTopics.Rating.Detail(
                it.formattedTotalReviews,
                it.percentageFloat,
                it.rate,
                it.totalReviews
            )
        }
    }
}
