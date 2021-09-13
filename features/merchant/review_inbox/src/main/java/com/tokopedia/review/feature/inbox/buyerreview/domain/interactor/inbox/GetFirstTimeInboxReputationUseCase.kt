package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox

import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudInboxReputationDataSource
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.usecase.RequestParams
import rx.Observable

/**
 * @author by nisie on 8/14/17.
 */
class GetFirstTimeInboxReputationUseCase constructor(
    private val getInboxReputationUseCase: GetInboxReputationUseCase,
    private val getCacheInboxReputationUseCase: GetCacheInboxReputationUseCase?,
    reputationRepository: ReputationRepository
) : GetInboxReputationUseCase(reputationRepository) {
    protected override var reputationRepository: ReputationRepository? = null
    public override fun createObservable(requestParams: RequestParams): Observable<InboxReputationDomain?> {
        return getInboxReputationUseCase.createObservable(requestParams)
    }

    companion object {
        val CACHE_REPUTATION: String = "CACHE_REPUTATION"
        val DURATION_CACHE: Int = 86400
        fun getFirstTimeParam(tab: Int): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.putInt(
                GetInboxReputationUseCase.Companion.PARAM_PER_PAGE,
                GetInboxReputationUseCase.Companion.DEFAULT_PER_PAGE
            )
            params.putInt(GetInboxReputationUseCase.Companion.PARAM_PAGE, 1)
            params.putInt(
                GetInboxReputationUseCase.Companion.PARAM_ROLE,
                GetInboxReputationUseCase.Companion.getRole(tab)
            )
            params.putString(
                GetInboxReputationUseCase.Companion.PARAM_TIME_FILTER,
                GetInboxReputationUseCase.Companion.DEFAULT_TIME_FILTER
            )
            params.putInt(
                GetInboxReputationUseCase.Companion.PARAM_STATUS,
                GetInboxReputationUseCase.Companion.getStatus(tab)
            )
            params.putInt(GetInboxReputationUseCase.Companion.PARAM_TAB, tab)
            params.putBoolean(CloudInboxReputationDataSource.Companion.IS_SAVE_TO_CACHE, true)
            return params
        }
    }
}