package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail

import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.CheckShopFavoriteDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationDetailDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1

/**
 * @author by nisie on 8/19/17.
 */
class GetInboxReputationDetailUseCase constructor(
    private val getInboxReputationUseCase: GetInboxReputationUseCase,
    private val getReviewUseCase: GetReviewUseCase,
    private val checkShopFavoritedUseCase: CheckShopFavoritedUseCase
) : UseCase<InboxReputationDetailDomain>() {
    public override fun createObservable(requestParams: RequestParams): Observable<InboxReputationDetailDomain> {
        val domain: InboxReputationDetailDomain = InboxReputationDetailDomain()
        return getReputation(domain, getReputationParam(requestParams))
            .flatMap(checkShopFavorited(requestParams))
            .flatMap(getReview(domain, getReviewParam(requestParams)))
    }

    private fun checkShopFavorited(requestParams: RequestParams): Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>> {
        return object :
            Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>> {
            public override fun call(inboxReputationDetailDomain: InboxReputationDetailDomain): Observable<InboxReputationDetailDomain> {
                if (inboxReputationDetailDomain.getInboxReputationDomain().getInboxReputation()
                        .get(0).getRevieweeData().getRevieweeRoleId() ==
                    InboxReputationItemUiModel.Companion.ROLE_SELLER
                ) {
                    return checkShopFavoritedUseCase.createObservable(
                        getShopFavoritedParam(
                            inboxReputationDetailDomain,
                            requestParams
                        )
                    )
                        .flatMap(object :
                            Func1<CheckShopFavoriteDomain, Observable<InboxReputationDetailDomain>> {
                            public override fun call(checkShopFavoriteDomain: CheckShopFavoriteDomain): Observable<InboxReputationDetailDomain> {
                                inboxReputationDetailDomain.getInboxReputationDomain()
                                    .getInboxReputation()
                                    .get(0).getRevieweeData().getRevieweeBadgeSeller()
                                    .setIsFavorited(
                                        if (checkShopFavoriteDomain
                                                .isShopFavorited()
                                        ) 1 else 0
                                    )
                                return Observable.just(inboxReputationDetailDomain)
                            }
                        })
                        .onErrorResumeNext(object :
                            Func1<Throwable?, Observable<out InboxReputationDetailDomain>> {
                            public override fun call(throwable: Throwable?): Observable<out InboxReputationDetailDomain> {
                                inboxReputationDetailDomain.getInboxReputationDomain()
                                    .getInboxReputation()
                                    .get(0).getRevieweeData().getRevieweeBadgeSeller()
                                    .setIsFavorited(-1)
                                return Observable.just(inboxReputationDetailDomain)
                            }
                        })
                } else {
                    inboxReputationDetailDomain.getInboxReputationDomain().getInboxReputation()
                        .get(0).getRevieweeData().getRevieweeBadgeSeller()
                        .setIsFavorited(-1)
                    return Observable.just(inboxReputationDetailDomain)
                }
            }
        }
    }

    private fun getShopFavoritedParam(
        domain: InboxReputationDetailDomain,
        requestParams: RequestParams
    ): RequestParams {
        return CheckShopFavoritedUseCase.Companion.getParam(
            requestParams.getString(GetReviewUseCase.Companion.PARAM_USER_ID, ""),
            domain.getInboxReputationDomain().getInboxReputation().get(0).getShopId()
        )
    }

    private fun getReputationParam(requestParams: RequestParams): RequestParams {
        return GetInboxReputationUseCase.Companion.getSpecificReputation(
            requestParams.getString(GetInboxReputationUseCase.Companion.PARAM_REPUTATION_ID, ""),
            requestParams.getInt(GetInboxReputationUseCase.Companion.PARAM_TAB, 0)
        )
    }

    private fun getReputation(
        domain: InboxReputationDetailDomain,
        reputationParam: RequestParams
    ): Observable<InboxReputationDetailDomain> {
        return getInboxReputationUseCase.createObservable(reputationParam)
            .flatMap(object :
                Func1<InboxReputationDomain?, Observable<InboxReputationDetailDomain>> {
                public override fun call(inboxReputationDomain: InboxReputationDomain?): Observable<InboxReputationDetailDomain> {
                    domain.setInboxReputationDomain(inboxReputationDomain)
                    return Observable.just(domain)
                }
            })
    }

    private fun getReviewParam(requestParams: RequestParams): RequestParams {
        return GetReviewUseCase.Companion.getParam(
            requestParams.getString(GetReviewUseCase.Companion.PARAM_REPUTATION_ID, ""),
            requestParams.getString(GetReviewUseCase.Companion.PARAM_USER_ID, ""),
            requestParams.getInt(GetInboxReputationUseCase.Companion.PARAM_TAB, 0)
        )
    }

    private fun getReview(
        domain: InboxReputationDetailDomain, reviewParam: RequestParams
    ): Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>> {
        return object :
            Func1<InboxReputationDetailDomain?, Observable<InboxReputationDetailDomain>> {
            public override fun call(inboxReputationDetailDomain: InboxReputationDetailDomain?): Observable<InboxReputationDetailDomain> {
                return getReviewUseCase.createObservable(reviewParam)
                    .flatMap(object :
                        Func1<ReviewDomain?, Observable<InboxReputationDetailDomain>> {
                        public override fun call(reviewDomain: ReviewDomain?): Observable<InboxReputationDetailDomain> {
                            domain.setReviewDomain(reviewDomain)
                            return Observable.just(domain)
                        }
                    })
            }
        }
    }

    companion object {
        fun getParam(reputationId: String?, userId: String?, tab: Int): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.getParameters().putAll(
                GetInboxReputationUseCase.Companion.getSpecificReputation(reputationId, tab)
                    .getParameters()
            )
            params.getParameters().putAll(
                GetReviewUseCase.Companion.getParam(reputationId, userId, tab)
                    .getParameters()
            )
            return params
        }
    }
}