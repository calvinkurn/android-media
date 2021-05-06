package com.tokopedia.tkpd.tkpdreputation.review.product.usecase

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ReviewProductGetRatingUseCase @Inject constructor(
        private val reputationRepository: ReputationRepository
) : UseCase<DataResponseReviewStarCount>() {

    companion object {
        private const val PARAM_PRODUCT_ID = "product_id"
    }

    private val params: RequestParams = RequestParams.create()

    override suspend fun executeOnBackground(): DataResponseReviewStarCount {
        return reputationRepository.getReviewStarCount(params)
    }

    fun setParams(productId: String) {
        params.putString(PARAM_PRODUCT_ID, productId)
    }

}