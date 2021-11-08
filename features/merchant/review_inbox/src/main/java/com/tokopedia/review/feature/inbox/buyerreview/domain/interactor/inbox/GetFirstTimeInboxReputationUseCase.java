package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox;


import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository;
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudInboxReputationDataSource;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain;
import com.tokopedia.usecase.RequestParams;
import rx.Observable;

/**
 * @author by nisie on 8/14/17.
 */

public class GetFirstTimeInboxReputationUseCase extends GetInboxReputationUseCase {

    public static final String CACHE_REPUTATION = "CACHE_REPUTATION";
    public static final int DURATION_CACHE = 86400;
    private final GetInboxReputationUseCase getInboxReputationUseCase;
    private final GetCacheInboxReputationUseCase getCacheInboxReputationUseCase;

    protected ReputationRepository reputationRepository;

    public GetFirstTimeInboxReputationUseCase(GetInboxReputationUseCase
                                                      getInboxReputationUseCase,
                                              GetCacheInboxReputationUseCase
                                                      getCacheInboxReputationUseCase,
                                              ReputationRepository reputationRepository) {
        super(reputationRepository);
        this.getInboxReputationUseCase = getInboxReputationUseCase;
        this.getCacheInboxReputationUseCase = getCacheInboxReputationUseCase;
    }

    @Override
    public Observable<InboxReputationDomain> createObservable(final RequestParams requestParams) {
        return getInboxReputationUseCase.createObservable(requestParams);
    }

    public static RequestParams getFirstTimeParam(int tab) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE);
        params.putInt(PARAM_PAGE, 1);
        params.putInt(PARAM_ROLE, getRole(tab));
        params.putString(PARAM_TIME_FILTER, DEFAULT_TIME_FILTER);
        params.putInt(PARAM_STATUS, getStatus(tab));
        params.putInt(PARAM_TAB, tab);
        params.putBoolean(CloudInboxReputationDataSource.IS_SAVE_TO_CACHE, true);
        return params;
    }
}
