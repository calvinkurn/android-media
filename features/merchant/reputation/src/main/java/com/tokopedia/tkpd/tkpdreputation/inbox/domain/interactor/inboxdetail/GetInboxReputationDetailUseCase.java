package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.CheckShopFavoriteDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 8/19/17.
 */

public class GetInboxReputationDetailUseCase extends UseCase<InboxReputationDetailDomain> {

    private final GetInboxReputationUseCase getInboxReputationUseCase;
    private final GetReviewUseCase getReviewUseCase;
    private final CheckShopFavoritedUseCase checkShopFavoritedUseCase;

    public GetInboxReputationDetailUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           GetInboxReputationUseCase getInboxReputationUseCase,
                                           GetReviewUseCase getReviewUseCase,
                                           CheckShopFavoritedUseCase checkShopFavoritedUseCase) {
        super(threadExecutor, postExecutionThread);
        this.getInboxReputationUseCase = getInboxReputationUseCase;
        this.getReviewUseCase = getReviewUseCase;
        this.checkShopFavoritedUseCase = checkShopFavoritedUseCase;
    }

    @Override
    public Observable<InboxReputationDetailDomain> createObservable(RequestParams requestParams) {
        InboxReputationDetailDomain domain = new InboxReputationDetailDomain();
        return getReputation(domain, getReputationParam(requestParams))
                .flatMap(checkShopFavorited(requestParams))
                .flatMap(getReview(domain, getReviewParam(requestParams)));
    }


    private Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>>
    checkShopFavorited(final RequestParams requestParams) {
        return new Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>>() {
            @Override
            public Observable<InboxReputationDetailDomain> call(final InboxReputationDetailDomain inboxReputationDetailDomain) {
                if (inboxReputationDetailDomain.getInboxReputationDomain().getInboxReputation()
                        .get(0).getRevieweeData().getRevieweeRoleId() ==
                        InboxReputationItemViewModel.ROLE_SELLER) {
                    return checkShopFavoritedUseCase.createObservable(getShopFavoritedParam(inboxReputationDetailDomain, requestParams))
                            .flatMap(new Func1<CheckShopFavoriteDomain, Observable<InboxReputationDetailDomain>>() {
                                @Override
                                public Observable<InboxReputationDetailDomain> call(CheckShopFavoriteDomain checkShopFavoriteDomain) {
                                    inboxReputationDetailDomain.getInboxReputationDomain().getInboxReputation()
                                            .get(0).getRevieweeData().getRevieweeBadgeSeller()
                                            .setIsFavorited(checkShopFavoriteDomain
                                                    .isShopFavorited() ? 1 : 0);
                                    return Observable.just(inboxReputationDetailDomain);
                                }
                            })
                            .onErrorResumeNext(new Func1<Throwable, Observable<? extends InboxReputationDetailDomain>>() {
                                @Override
                                public Observable<? extends InboxReputationDetailDomain> call(Throwable throwable) {
                                    inboxReputationDetailDomain.getInboxReputationDomain().getInboxReputation()
                                            .get(0).getRevieweeData().getRevieweeBadgeSeller()
                                            .setIsFavorited(-1);
                                    return Observable.just(inboxReputationDetailDomain);
                                }
                            });
                } else {
                    inboxReputationDetailDomain.getInboxReputationDomain().getInboxReputation()
                            .get(0).getRevieweeData().getRevieweeBadgeSeller()
                            .setIsFavorited(-1);
                    return Observable.just(inboxReputationDetailDomain);
                }
            }
        };
    }

    private RequestParams getShopFavoritedParam(InboxReputationDetailDomain domain, RequestParams requestParams) {
        return CheckShopFavoritedUseCase.getParam(
                requestParams.getString(GetReviewUseCase.PARAM_USER_ID, ""),
                domain.getInboxReputationDomain().getInboxReputation().get(0).getShopId()
        );
    }

    private RequestParams getReputationParam(RequestParams requestParams) {
        return GetInboxReputationUseCase.getSpecificReputation(
                requestParams.getString(GetInboxReputationUseCase.PARAM_REPUTATION_ID, ""),
                requestParams.getInt(GetInboxReputationUseCase.PARAM_TAB, 0)
        );
    }

    private Observable<InboxReputationDetailDomain> getReputation(final InboxReputationDetailDomain domain, RequestParams reputationParam) {
        return getInboxReputationUseCase.createObservable(reputationParam)
                .flatMap(new Func1<InboxReputationDomain, Observable<InboxReputationDetailDomain>>() {
                    @Override
                    public Observable<InboxReputationDetailDomain> call(InboxReputationDomain inboxReputationDomain) {
                        domain.setInboxReputationDomain(inboxReputationDomain);
                        return Observable.just(domain);
                    }
                });
    }


    private RequestParams getReviewParam(RequestParams requestParams) {
        return GetReviewUseCase.getParam(
                requestParams.getString(GetReviewUseCase.PARAM_REPUTATION_ID, ""),
                requestParams.getString(GetReviewUseCase.PARAM_USER_ID, ""),
                requestParams.getInt(GetInboxReputationUseCase.PARAM_TAB, 0)
        );
    }

    private Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>> getReview(
            final InboxReputationDetailDomain domain, final RequestParams reviewParam) {
        return new Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>>() {
            @Override
            public Observable<InboxReputationDetailDomain> call(InboxReputationDetailDomain inboxReputationDetailDomain) {
                return getReviewUseCase.createObservable(reviewParam)
                        .flatMap(new Func1<ReviewDomain, Observable<InboxReputationDetailDomain>>() {
                            @Override
                            public Observable<InboxReputationDetailDomain> call(ReviewDomain reviewDomain) {
                                domain.setReviewDomain(reviewDomain);
                                return Observable.just(domain);
                            }
                        });
            }
        };
    }

    public static RequestParams getParam(String reputationId, String userId, int tab) {
        RequestParams params = RequestParams.create();
        params.getParameters().putAll(GetInboxReputationUseCase.getSpecificReputation
                (reputationId, tab).getParameters());
        params.getParameters().putAll(GetReviewUseCase.getParam(reputationId, userId, tab)
                .getParameters());
        return params;
    }
}
