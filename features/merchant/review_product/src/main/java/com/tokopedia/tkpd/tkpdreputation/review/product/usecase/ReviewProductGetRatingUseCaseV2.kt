package com.tokopedia.tkpd.tkpdreputation.review.product.usecase

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepositoryV2
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ReviewProductGetRatingUseCaseV2 @Inject constructor(
        private val reputationRepository: ReputationRepositoryV2
) : UseCase<DataResponseReviewStarCount>() {

    companion object {
        const val PRODUCT_ID = "product_id"
    }

    val params = hashMapOf<String, String>()

    override suspend fun executeOnBackground(): DataResponseReviewStarCount {
        return reputationRepository.getReviewStarCount(params[PRODUCT_ID] ?: "")
    }

}