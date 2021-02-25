package com.tokopedia.tkpd.tkpdreputation.review.product.usecase

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepositoryV2
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ReviewProductGetRatingUseCaseV2 @Inject constructor(
        private val reputationRepository: ReputationRepositoryV2
) : UseCase<DataResponseReviewStarCount>() {

    companion object {
        const val PRODUCT_ID = "product_id"
    }

    lateinit var params: Params

    override suspend fun executeOnBackground(): DataResponseReviewStarCount {
        if (::params.isInitialized) {
            return reputationRepository.getReviewStarCount(params.productId)
        } else throw ErrorMessageException("params not initialized")
    }

    data class Params(
            val productId: String
    )

}