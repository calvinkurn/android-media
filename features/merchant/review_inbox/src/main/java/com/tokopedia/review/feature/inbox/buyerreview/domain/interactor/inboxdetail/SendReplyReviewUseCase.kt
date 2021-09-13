package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail

import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendReplyReviewDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 9/28/17.
 */
class SendReplyReviewUseCase constructor(protected var reputationRepository: ReputationRepository) :
    UseCase<SendReplyReviewDomain?>() {
    public override fun createObservable(requestParams: RequestParams): Observable<SendReplyReviewDomain?> {
        return (reputationRepository.insertReviewResponse(requestParams))!!
    }

    companion object {
        private val PARAM_REVIEW_ID: String = "review_id"
        private val PARAM_PRODUCT_ID: String = "product_id"
        private val PARAM_SHOP_ID: String = "shop_id"
        private val PARAM_REPUTATION_ID: String = "reputation_id"
        private val PARAM_RESPONSE_MESSAGE: String = "response_message"
        private val PARAM_ACTION: String = "action"
        private val ACTION_: String = "action"
        fun getParam(
            reputationId: String?, productId: String?,
            shopId: String?, reviewId: String?, replyReview: String?
        ): RequestParams {
            val requestParams: RequestParams = RequestParams.create()
            requestParams.putString(PARAM_REVIEW_ID, reviewId)
            requestParams.putString(PARAM_PRODUCT_ID, productId)
            requestParams.putString(PARAM_SHOP_ID, shopId)
            requestParams.putString(PARAM_REPUTATION_ID, reputationId)
            requestParams.putString(PARAM_RESPONSE_MESSAGE, replyReview)
            return requestParams
        }
    }
}