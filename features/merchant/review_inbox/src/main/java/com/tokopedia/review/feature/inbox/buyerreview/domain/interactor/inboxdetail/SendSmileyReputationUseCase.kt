package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail

import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 8/31/17.
 */
class SendSmileyReputationUseCase constructor(private val reputationRepository: ReputationRepository) :
    UseCase<SendSmileyReputationDomain?>() {
    public override fun createObservable(requestParams: RequestParams): Observable<SendSmileyReputationDomain?> {
        return (reputationRepository.sendSmiley(requestParams))!!
    }

    companion object {
        private val PARAM_SCORE: String = "reputation_score"
        private val PARAM_REPUTATION_ID: String = "reputation_id"
        private val PARAM_ROLE: String = "buyer_seller"
        private val I_AM_SELLER: String = "2"
        private val I_AM_BUYER: String = "1"
        private val REVIEW_IS_FROM_BUYER: Int = 1
        fun getParam(reputationId: String?, score: String?, role: Int): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.putString(PARAM_SCORE, score)
            params.putString(PARAM_REPUTATION_ID, reputationId)
            params.putString(PARAM_ROLE, getRole(role))
            return params
        }

        private fun getRole(role: Int): String {
            return if (role == REVIEW_IS_FROM_BUYER) I_AM_SELLER else I_AM_BUYER
        }
    }
}