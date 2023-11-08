package com.tokopedia.shop.info.data.mapper

import com.tokopedia.shop.info.data.response.ProductRevGetShopRatingAndTopicsResponse
import com.tokopedia.shop.info.domain.entity.ShopRating
import javax.inject.Inject

class ProductRevGetShopRatingAndTopicsMapper @Inject constructor() {
    fun map(response: ProductRevGetShopRatingAndTopicsResponse): ShopRating {
        val rating = response.productrevGetShopRatingAndTopics.rating

        return ShopRating(
            detail = rating.toDetail(),
            positivePercentageFmt = rating.positivePercentageFmt,
            ratingScore = rating.ratingScore,
            totalRating = rating.totalRating,
            totalRatingFmt = rating.totalRatingFmt,
            totalRatingTextAndImage = rating.totalRatingTextAndImage,
            totalRatingTextAndImageFmt = rating.totalRatingTextAndImageFmt
        )
    }

    private fun ProductRevGetShopRatingAndTopicsResponse.ProductrevGetShopRatingAndTopics.Rating.toDetail(): List<ShopRating.Detail> {
        return detail.map {
            ShopRating.Detail(
                it.formattedTotalReviews,
                it.percentageFloat,
                it.rate,
                it.totalReviews
            )
        }
    }
}
