package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail

import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 9/26/17.
 */
class GetReviewUseCase @Inject constructor(private var reputationRepository: ReputationRepository) :
    UseCase<ReviewDomain>() {

    override fun createObservable(requestParams: RequestParams): Observable<ReviewDomain> {
        return (reputationRepository.getReviewFromCloud(requestParams))
    }

    companion object {
        const val PARAM_REPUTATION_ID: String = "reputation_id"
        const val PARAM_USER_ID: String = "user_id"

        private const val PARAM_ROLE: String = "role"
        private const val ROLE_BUYER: Int = 1
        private const val ROLE_SELLER: Int = 2

        fun getParam(id: String?, userId: String?, tab: Int): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.putString(PARAM_REPUTATION_ID, id)
            params.putString(PARAM_USER_ID, userId)
            params.putInt(PARAM_ROLE, getRole(tab))
            return params
        }

        fun getRole(tab: Int): Int {
            return when (tab) {
                ReviewInboxConstants.TAB_WAITING_REVIEW -> ROLE_BUYER
                ReviewInboxConstants.TAB_MY_REVIEW -> ROLE_BUYER
                ReviewInboxConstants.TAB_BUYER_REVIEW -> ROLE_SELLER
                else -> ROLE_BUYER
            }
        }
    }
}