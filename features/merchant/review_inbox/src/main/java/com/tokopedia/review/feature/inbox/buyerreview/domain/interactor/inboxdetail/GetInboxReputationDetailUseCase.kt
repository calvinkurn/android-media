package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail

import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationDetailDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
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
    private val getReviewUseCase: GetReviewUseCase,
    private val checkShopFavoritedUseCase: CheckShopFavoritedUseCase
) : UseCase<InboxReputationDetailDomain>() {

    override fun createObservable(requestParams: RequestParams): Observable<InboxReputationDetailDomain> {
        val domain = InboxReputationDetailDomain()
        return getReputation(domain, getReputationParam(requestParams))
            .flatMap(checkShopFavorited(requestParams))
            .flatMap(getReview(domain, getReviewParam(requestParams)))
    }

    private fun checkShopFavorited(requestParams: RequestParams): Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>> {
        return object :
            Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>> {
            override fun call(inboxReputationDetailDomain: InboxReputationDetailDomain): Observable<InboxReputationDetailDomain> {
                if (inboxReputationDetailDomain.inboxReputationResponse.reputationList.firstOrNull()?.revieweeData?.roleId ==
                    InboxReputationItemUiModel.ROLE_SELLER) {
                    return checkShopFavoritedUseCase.createObservable(
                        getShopFavoritedParam(
                            inboxReputationDetailDomain,
                            requestParams
                        )
                    )
                        .flatMap { checkShopFavoriteDomain ->
                            inboxReputationDetailDomain.inboxReputationResponse
                                .reputationList
                                .firstOrNull()?.revieweeData?.shopBadge
                                ?.isFavorited =
                                if (checkShopFavoriteDomain.isShopFavorited) 1 else 0

                            Observable.just(inboxReputationDetailDomain)
                        }
                        .onErrorResumeNext {
                            inboxReputationDetailDomain.inboxReputationResponse
                                .reputationList
                                .firstOrNull()?.revieweeData?.shopBadge?.isFavorited = -1
                            Observable.just(inboxReputationDetailDomain)
                        }
                } else {
                    inboxReputationDetailDomain.inboxReputationResponse.reputationList
                        .firstOrNull()?.revieweeData?.shopBadge?.isFavorited = -1
                    return Observable.just(inboxReputationDetailDomain)
                }
            }
        }
    }

    private fun getShopFavoritedParam(
        domain: InboxReputationDetailDomain,
        requestParams: RequestParams
    ): RequestParams {
        return CheckShopFavoritedUseCase.getParam(
            requestParams.getString(GetReviewUseCase.PARAM_USER_ID, ""),
            domain.inboxReputationResponse.reputationList.firstOrNull()?.shopIdStr ?:""
        )
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