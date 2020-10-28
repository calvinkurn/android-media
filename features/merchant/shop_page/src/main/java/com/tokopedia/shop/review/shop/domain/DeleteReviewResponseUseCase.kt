package com.tokopedia.shop.review.shop.domain

import com.tokopedia.shop.review.shop.domain.model.DeleteReviewResponseDomain
import com.tokopedia.shop.review.shop.domain.repository.ReputationRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 9/27/17.
 */
class DeleteReviewResponseUseCase(private val reputationRepository: ReputationRepository) : UseCase<DeleteReviewResponseDomain>() {
    override fun createObservable(requestParams: RequestParams): Observable<DeleteReviewResponseDomain> {
        return reputationRepository.deleteReviewResponse(requestParams)
    }

    companion object {
        private const val PARAM_REVIEW_ID = "review_id"
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_REPUTATION_ID = "reputation_id"
        fun getParam(reviewId: String?, productId: String?, shopId: String?, reputationId: String?): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_REVIEW_ID, reviewId)
            requestParams.putString(PARAM_PRODUCT_ID, productId)
            requestParams.putString(PARAM_SHOP_ID, shopId)
            requestParams.putString(PARAM_REPUTATION_ID, reputationId)
            return requestParams
        }
    }

}