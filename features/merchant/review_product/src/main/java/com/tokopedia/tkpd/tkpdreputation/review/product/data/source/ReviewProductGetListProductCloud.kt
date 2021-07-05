package com.tokopedia.tkpd.tkpdreputation.review.product.data.source

import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductService
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct
import com.tokopedia.usecase.RequestParams

class ReviewProductGetListProductCloud(
        private val reviewProductService: ReviewProductService
) {
    suspend fun getReviewProductList(params: RequestParams): DataResponseReviewProduct {
        return reviewProductService.api.getReviewProductList(params.paramsAllValueInString).body()!!.data
    }
}