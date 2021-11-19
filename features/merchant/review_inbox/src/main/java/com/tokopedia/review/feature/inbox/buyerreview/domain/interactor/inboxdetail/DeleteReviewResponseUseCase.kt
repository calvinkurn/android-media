package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail

import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 9/27/17.
 */
class DeleteReviewResponseUseCase @Inject constructor(private val reputationRepository: ReputationRepository) :
    UseCase<DeleteReviewResponseDomain>() {

    override fun createObservable(requestParams: RequestParams): Observable<DeleteReviewResponseDomain> {
        return (reputationRepository.deleteReviewResponse(requestParams))
    }

    companion object {
        private const val PARAM_REVIEW_ID: String = "review_id"
        private const val PARAM_PRODUCT_ID: String = "product_id"
        private const val PARAM_SHOP_ID: String = "shop_id"
        private const val PARAM_REPUTATION_ID: String = "reputation_id"

        fun getParam(
            reviewId: String?,
            productId: String?,
            shopId: String?,
            reputationId: String?
        ): RequestParams {
            val requestParams: RequestParams = RequestParams.create()
            requestParams.putString(PARAM_REVIEW_ID, reviewId)
            requestParams.putString(PARAM_PRODUCT_ID, productId)
            requestParams.putString(PARAM_SHOP_ID, shopId)
            requestParams.putString(PARAM_REPUTATION_ID, reputationId)
            return requestParams
        }
    }
}