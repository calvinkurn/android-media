package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox

import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudInboxReputationDataSource.Companion.IS_SAVE_TO_CACHE
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 8/14/17.
 */
class GetFirstTimeInboxReputationUseCase @Inject constructor(
    private val getInboxReputationUseCase: GetInboxReputationUseCase,
    reputationRepository: ReputationRepository
) : GetInboxReputationUseCase(reputationRepository) {

    override fun createObservable(requestParams: RequestParams): Observable<InboxReputationDomain> {
        return getInboxReputationUseCase.createObservable(requestParams)
    }

    companion object {
        const val CACHE_REPUTATION: String = "CACHE_REPUTATION"
        const val DURATION_CACHE: Int = 86400

        fun getFirstTimeParam(tab: Int): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.putInt(
                PARAM_PER_PAGE,
                DEFAULT_PER_PAGE
            )
            params.putInt(PARAM_PAGE, 1)
            params.putInt(
                PARAM_ROLE,
                getRole(tab)
            )
            params.putString(
                PARAM_TIME_FILTER,
                DEFAULT_TIME_FILTER
            )
            params.putInt(
                PARAM_STATUS,
                getStatus(tab)
            )
            params.putInt(PARAM_TAB, tab)
            params.putBoolean(IS_SAVE_TO_CACHE, true)
            return params
        }
    }
}