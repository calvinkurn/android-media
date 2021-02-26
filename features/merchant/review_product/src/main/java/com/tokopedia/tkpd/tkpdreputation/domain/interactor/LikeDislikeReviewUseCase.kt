package com.tokopedia.tkpd.tkpdreputation.domain.interactor

import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException
import com.tokopedia.usecase.coroutines.UseCase

class LikeDislikeReviewUseCase(
        private val reputationRepository: ReputationRepository
) : UseCase<LikeDislikeDomain>() {

    companion object {
        private const val PARAM_REVIEW_ID = "review_id"
        private const val PARAM_LIKE_STATUS = "like_status"
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_ACTION = "action"
        private const val ACTION_LIKE_DISLIKE_REVIEW = "event_like_dislike_review"
    }

    lateinit var params: Params

    override suspend fun executeOnBackground(): LikeDislikeDomain {
        if (::params.isInitialized) {
            return reputationRepository.likeDislikeReview(mapOf(
                    PARAM_REVIEW_ID to params.reviewId,
                    PARAM_LIKE_STATUS to params.likeStatus.toString(),
                    PARAM_PRODUCT_ID to params.productId,
                    PARAM_SHOP_ID to params.shopId,
                    PARAM_ACTION to params.action
            ))
        } else throw ErrorMessageException("params not initialized")
    }

    data class Params(
            val reviewId: String,
            val likeStatus: Int,
            val productId: String,
            val shopId: String,
            val action: String = ACTION_LIKE_DISLIKE_REVIEW
    )

}