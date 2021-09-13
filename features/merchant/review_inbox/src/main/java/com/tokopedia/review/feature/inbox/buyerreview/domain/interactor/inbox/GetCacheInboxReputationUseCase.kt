package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox

import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 9/20/17.
 */
class GetCacheInboxReputationUseCase @Inject constructor(private val reputationRepository: ReputationRepository) :
    UseCase<InboxReputationDomain>() {

    override fun createObservable(requestParams: RequestParams): Observable<InboxReputationDomain> {
        return (reputationRepository.getInboxReputationFromLocal(requestParams))
    }
}