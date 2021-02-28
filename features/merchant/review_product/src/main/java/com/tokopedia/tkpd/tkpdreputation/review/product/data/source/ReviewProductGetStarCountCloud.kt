package com.tokopedia.tkpd.tkpdreputation.review.product.data.source

import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductService
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import com.tokopedia.tkpd.tkpdreputation.utils.ReputationUtil
import com.tokopedia.usecase.RequestParams

class ReviewProductGetStarCountCloud(
        private val reviewProductService: ReviewProductService
) {
    suspend fun getReviewStarCount(params: RequestParams): DataResponseReviewStarCount {
        return reviewProductService.api.getReviewStarCount(
                ReputationUtil.convertMapObjectToString(params.parameters)
        ).body()!!.data
    }
}