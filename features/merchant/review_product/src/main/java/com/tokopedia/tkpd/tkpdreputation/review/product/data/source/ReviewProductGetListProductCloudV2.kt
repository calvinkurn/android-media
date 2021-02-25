package com.tokopedia.tkpd.tkpdreputation.review.product.data.source

import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductServiceV2
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct

class ReviewProductGetListProductCloudV2(
        private val reviewProductService: ReviewProductServiceV2
) {

    companion object {
        private const val PRODUCT_ID = "product_id"
        private const val PAGE = "page"
        private const val PER_PAGE = "per_page"
        private const val RATING = "rating"
        private const val WITH_ATTACHMENT = "with_attachment"
    }

    suspend fun getReviewProductList(
            productId: String,
            page: String,
            perPage: String,
            rating: String,
            withAttachment: String
    ): DataResponseReviewProduct {

        val params = mapOf(
                PRODUCT_ID to productId,
                PAGE to page,
                PER_PAGE to perPage,
                RATING to rating,
                WITH_ATTACHMENT to withAttachment
        )

        return reviewProductService.api!!.getReviewProductList(params).body()!!.data
    }

}