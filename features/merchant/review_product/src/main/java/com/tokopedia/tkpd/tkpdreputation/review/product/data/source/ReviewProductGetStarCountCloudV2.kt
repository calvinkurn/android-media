package com.tokopedia.tkpd.tkpdreputation.review.product.data.source

import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductServiceV2
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount

class ReviewProductGetStarCountCloudV2(
        private val reviewProductService: ReviewProductServiceV2
) {

    companion object {
        const val PRODUCT_ID = "product_id"
    }

    suspend fun getReviewStarCount(productId: String): DataResponseReviewStarCount {
        val params = mapOf(PRODUCT_ID to productId)
        return reviewProductService.api!!.getReviewStarCount(params).body()!!.data
    }
}