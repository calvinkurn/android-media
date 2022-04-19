package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox

import android.text.TextUtils
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 8/18/17.
 */
open class GetInboxReputationUseCase @Inject constructor(protected var reputationRepository: ReputationRepository) :
    UseCase<InboxReputationDomain>() {

    override fun createObservable(requestParams: RequestParams): Observable<InboxReputationDomain> {
        return (reputationRepository.getInboxReputationFromCloud(requestParams))
    }

    companion object {
        const val PARAM_PER_PAGE: String = "per_page"
        const val PARAM_PAGE: String = "page"
        const val PARAM_ROLE: String = "role"
        const val DEFAULT_PER_PAGE: Int = 10
        const val PARAM_TIME_FILTER: String = "time_filter"
        const val PARAM_STATUS: String = "status"
        const val PARAM_SCORE_FILTER: String = "score_filter"
        const val PARAM_TAB: String = "tab"
        const val PARAM_REPUTATION_ID: String = "reputation_id"

        private const val STATUS_UNASSESSED_REPUTATION: Int = 1
        private const val STATUS_UPDATED_REPUTATION: Int = 2
        private const val ROLE_BUYER: Int = 1
        private const val ROLE_SELLER: Int = 2
        private const val STATUS_OTHER: Int = 3
        const val DEFAULT_TIME_FILTER: String = "1"
        private const val PARAM_KEYWORD: String = "keyword"

        fun getParam(
            page: Int, keyword: String?, timeFilter: String?,
            scoreFilter: String?, tab: Int
        ): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE)
            params.putInt(PARAM_PAGE, page)
            params.putInt(PARAM_ROLE, getRole(tab))
            if (!TextUtils.isEmpty(keyword)) params.putString(PARAM_KEYWORD, keyword)
            params.putString(
                PARAM_TIME_FILTER,
                if (!TextUtils.isEmpty(timeFilter)) timeFilter else DEFAULT_TIME_FILTER
            )
            if (!TextUtils.isEmpty(scoreFilter)) params.putString(PARAM_SCORE_FILTER, scoreFilter)
            params.putInt(PARAM_STATUS, getStatus(tab))
            params.putInt(PARAM_TAB, tab)
            return params
        }

        fun getSpecificReputation(reputationId: String?, tab: Int): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE)
            params.putInt(PARAM_PAGE, 1)
            params.putInt(PARAM_ROLE, getRole(tab))
            params.putString(PARAM_TIME_FILTER, DEFAULT_TIME_FILTER)
            params.putInt(PARAM_STATUS, getStatus(tab))
            params.putInt(PARAM_TAB, tab)
            params.putString(PARAM_REPUTATION_ID, reputationId)
            return params
        }

        fun getStatus(tab: Int): Int {
            return when (tab) {
                ReviewInboxConstants.TAB_WAITING_REVIEW -> STATUS_UNASSESSED_REPUTATION
                ReviewInboxConstants.TAB_MY_REVIEW -> STATUS_UPDATED_REPUTATION
                ReviewInboxConstants.TAB_BUYER_REVIEW -> STATUS_OTHER
                else -> STATUS_OTHER
            }
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