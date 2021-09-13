package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox

import android.text.TextUtils
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 8/18/17.
 */
open class GetInboxReputationUseCase constructor(protected var reputationRepository: ReputationRepository) :
    UseCase<InboxReputationDomain?>() {
    public override fun createObservable(requestParams: RequestParams): Observable<InboxReputationDomain?> {
        return (reputationRepository.getInboxReputationFromCloud(requestParams))!!
    }

    companion object {
        protected val PARAM_PER_PAGE: String = "per_page"
        protected val PARAM_PAGE: String = "page"
        protected val PARAM_ROLE: String = "role"
        protected val DEFAULT_PER_PAGE: Int = 10
        val PARAM_TIME_FILTER: String = "time_filter"
        val PARAM_STATUS: String = "status"
        val PARAM_SCORE_FILTER: String = "score_filter"
        protected val STATUS_UNASSESSED_REPUTATION: Int = 1
        protected val STATUS_UPDATED_REPUTATION: Int = 2
        protected val ROLE_BUYER: Int = 1
        protected val ROLE_SELLER: Int = 2
        protected val STATUS_OTHER: Int = 3
        protected val DEFAULT_TIME_FILTER: String = "1"
        private val PARAM_KEYWORD: String = "keyword"
        val PARAM_TAB: String = "tab"
        val PARAM_REPUTATION_ID: String = "reputation_id"
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

        protected fun getStatus(tab: Int): Int {
            when (tab) {
                ReviewInboxConstants.TAB_WAITING_REVIEW -> return STATUS_UNASSESSED_REPUTATION
                ReviewInboxConstants.TAB_MY_REVIEW -> return STATUS_UPDATED_REPUTATION
                ReviewInboxConstants.TAB_BUYER_REVIEW -> return STATUS_OTHER
                else -> return STATUS_OTHER
            }
        }

        protected fun getRole(tab: Int): Int {
            when (tab) {
                ReviewInboxConstants.TAB_WAITING_REVIEW -> return ROLE_BUYER
                ReviewInboxConstants.TAB_MY_REVIEW -> return ROLE_BUYER
                ReviewInboxConstants.TAB_BUYER_REVIEW -> return ROLE_SELLER
                else -> return ROLE_BUYER
            }
        }
    }
}