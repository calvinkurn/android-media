package com.tokopedia.shop.info.data.mapper

import com.tokopedia.shop.info.data.response.ProductRevGetShopReviewReadingListResponse
import com.tokopedia.shop.info.domain.entity.Review
import javax.inject.Inject

class ProductRevGetShopReviewReadingListMapper @Inject constructor(){
    fun map(response: ProductRevGetShopReviewReadingListResponse) : List<Review> {
        return response.productrevGetShopReviewReadingList.list.map { 
            Review(it.rating, it.reviewTime, it.reviewText, it.reviewerName)
        }
    }
}
