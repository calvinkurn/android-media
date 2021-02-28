package com.tokopedia.tkpd.tkpdreputation.review.product.data.source

import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductService
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful
import com.tokopedia.tkpd.tkpdreputation.utils.ReputationUtil
import com.tokopedia.usecase.RequestParams

class ReviewProductGetHelpfulReviewCloud(
        private val reviewProductService: ReviewProductService
) {
    suspend fun getReviewHelpfulList(params: RequestParams): DataResponseReviewHelpful {
        return reviewProductService.api.getReviewHelpfulList(
                params.paramsAllValueInString
        ).body()!!.data
    }
}