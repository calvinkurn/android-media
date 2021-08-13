package com.tokopedia.tkpd.tkpdreputation.domain.interactor

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.usecase.RequestParams
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

    private val params = RequestParams.create()

    override suspend fun executeOnBackground(): DeleteReviewResponseDomain {
        return reputationRepository.deleteReviewResponse(params)
    }

    fun setParams(reviewId: String, productId: String, shopId: String, reputationId: String) {
        params.putString(PARAM_REVIEW_ID, reviewId)
        params.putString(PARAM_PRODUCT_ID, productId)
        params.putString(PARAM_SHOP_ID, shopId)
        params.putString(PARAM_REPUTATION_ID, reputationId)
    }
}