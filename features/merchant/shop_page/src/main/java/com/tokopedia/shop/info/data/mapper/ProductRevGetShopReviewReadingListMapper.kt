package com.tokopedia.shop.info.data.mapper

import com.tokopedia.shop.info.data.response.ProductRevGetShopReviewReadingListResponse
import com.tokopedia.shop.info.domain.entity.ShopReview
import javax.inject.Inject

class ProductRevGetShopReviewReadingListMapper @Inject constructor(){
    fun map(response: ProductRevGetShopReviewReadingListResponse) : ShopReview {
        return ShopReview(
            totalReviews = response.productrevGetShopReviewReadingList.totalReviews,
            reviews = response.productrevGetShopReviewReadingList.list.map { review ->
                ShopReview.Review(
                    review.rating,
                    review.reviewTime,
                    review.reviewText,
                    review.reviewerName,
                    review.reviewerLabel,
                    ShopReview.Review.LikeDislike(
                        review.likeDislike.totalLike,
                        review.likeDislike.totalLike
                    ),
                    review.avatar
                )
            }
        )
    }
}
