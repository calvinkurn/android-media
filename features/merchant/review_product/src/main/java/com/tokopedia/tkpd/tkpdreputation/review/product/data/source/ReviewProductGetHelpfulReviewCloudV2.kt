package com.tokopedia.tkpd.tkpdreputation.review.product.data.source

import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductServiceV2
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful

class ReviewProductGetHelpfulReviewCloudV2(
        private val reviewProductService: ReviewProductServiceV2
) {

    companion object {
        const val PRODUCT_ID = "product_id"
        const val SHOP_ID = "shop_id"
    }

    suspend fun getReviewHelpfulList(shopId: String, productId: String): DataResponseReviewHelpful {
        val params = mapOf(SHOP_ID to shopId, PRODUCT_ID to productId)
        return reviewProductService.api!!.getReviewHelpfulList(params).body()!!.data
    }
}