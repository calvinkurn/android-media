package com.tokopedia.tkpd.tkpdreputation.domain.interactor

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException
import com.tokopedia.usecase.coroutines.UseCase

class DeleteReviewResponseUseCase(
        private val reputationRepository: ReputationRepository
) : UseCase<DeleteReviewResponseDomain>() {

    companion object {
        private const val PARAM_REVIEW_ID = "review_id"
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_REPUTATION_ID = "reputation_id"
    }

    lateinit var params: Params

    override suspend fun executeOnBackground(): DeleteReviewResponseDomain {
        if (::params.isInitialized) {
            return reputationRepository.deleteReviewResponse(mapOf(
                    PARAM_REVIEW_ID to params.reviewId,
                    PARAM_PRODUCT_ID to params.productId,
                    PARAM_SHOP_ID to params.shopId,
                    PARAM_REPUTATION_ID to params.reputationId
            ))
        } else throw ErrorMessageException("params not initialized")
    }

    data class Params(
            val reviewId: String,
            val productId: String,
            val shopId: String,
            val reputationId: String
    )

}