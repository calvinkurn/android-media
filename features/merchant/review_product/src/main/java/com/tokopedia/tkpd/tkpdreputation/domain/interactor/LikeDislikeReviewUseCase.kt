package com.tokopedia.tkpd.tkpdreputation.domain.interactor

import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.usecase.RequestParams
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

    private val params = RequestParams.create()

    override suspend fun executeOnBackground(): LikeDislikeDomain {
        return reputationRepository.likeDislikeReview(params)
    }

    fun setParams(
            reviewId: String,
            likeStatus: Int,
            productId: String,
            shopId: String,
            action: String = ACTION_LIKE_DISLIKE_REVIEW
    ) {
        params.putString(PARAM_REVIEW_ID, reviewId)
        params.putString(PARAM_LIKE_STATUS, likeStatus.toString())
        params.putString(PARAM_PRODUCT_ID, productId)
        params.putString(PARAM_SHOP_ID, shopId)
        params.putString(PARAM_ACTION, action)
    }
}