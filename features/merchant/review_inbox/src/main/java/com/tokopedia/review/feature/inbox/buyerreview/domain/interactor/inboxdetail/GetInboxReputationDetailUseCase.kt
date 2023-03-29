package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail

import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationDetailDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject


/**
 * @author by nisie on 8/19/17.
 */
class GetInboxReputationDetailUseCase @Inject constructor(
    private val getInboxReputationUseCase: GetInboxReputationUseCase,
    private val getReviewUseCase: GetReviewUseCase
) : UseCase<InboxReputationDetailDomain>() {

    override fun createObservable(requestParams: RequestParams): Observable<InboxReputationDetailDomain> {
        val domain = InboxReputationDetailDomain()
        return getReputation(domain, getReputationParam(requestParams))
            .flatMap(getReview(domain, getReviewParam(requestParams)))
    }

    private fun getReputationParam(requestParams: RequestParams): RequestParams {
        return GetInboxReputationUseCase.getSpecificReputation(
            requestParams.getString(GetInboxReputationUseCase.PARAM_REPUTATION_ID, ""),
            requestParams.getInt(GetInboxReputationUseCase.PARAM_STATUS, 0)
        )
    }

    private fun getReputation(
        domain: InboxReputationDetailDomain,
        reputationParam: RequestParams
    ): Observable<InboxReputationDetailDomain> {
        return getInboxReputationUseCase.createObservable(reputationParam)
            .flatMap { inboxReputationDomain ->
                domain.inboxReputationResponse = inboxReputationDomain
                Observable.just(domain)
            }
    }

    private fun getReviewParam(requestParams: RequestParams): RequestParams {
        return GetReviewUseCase.getParam(
            requestParams.getString(GetReviewUseCase.PARAM_REPUTATION_ID, ""),
            requestParams.getInt(GetInboxReputationUseCase.PARAM_STATUS, 0)
        )
    }

    private fun getReview(
        domain: InboxReputationDetailDomain, reviewParam: RequestParams
    ): Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>> {
        return Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>> {
            getReviewUseCase.createObservable(reviewParam)
                .flatMap { reviewDomain ->
                    domain.reviewDomain = reviewDomain
                    Observable.just(domain)
                }
        }
    }

    companion object {
        fun getParam(reputationId: String, tab: Int): RequestParams {
            val params = RequestParams.create()
            params.parameters.putAll(
                GetInboxReputationUseCase.getSpecificReputation(
                    reputationId,
                    tab
                ).parameters
            )
            params.parameters.putAll(
                GetReviewUseCase.getParam(reputationId, tab)
                    .parameters
            )
            return params
        }
    }
}