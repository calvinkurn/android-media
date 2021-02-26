package com.tokopedia.tkpd.tkpdreputation.review.product.usecase

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ReviewProductGetRatingUseCase @Inject constructor(
        private val reputationRepository: ReputationRepository
) : UseCase<DataResponseReviewStarCount>() {

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