package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 8/14/17.
 */

public class CloudInboxReputationDataSource {
    public static final String IS_SAVE_TO_CACHE = "IS_SAVE_TO_CACHE";
    private final InboxReputationMapper inboxReputationMapper;
    private final GlobalCacheManager globalCacheManager;
    private final ReputationService reputationService;

    public CloudInboxReputationDataSource(ReputationService reputationService,
                                          InboxReputationMapper inboxReputationMapper,
                                          GlobalCacheManager globalCacheManager) {

        this.reputationService = reputationService;
        this.inboxReputationMapper = inboxReputationMapper;
        this.globalCacheManager = globalCacheManager;
    }


    public Observable<InboxReputationDomain> getInboxReputation(RequestParams requestParams) {
        return reputationService.getApi().getInbox(
                AuthUtil.generateParamsNetwork2(
                        MainApplication.getAppContext(),
                        requestParams.getParameters()))
                .map(inboxReputationMapper)
                .doOnNext(saveToCache(requestParams));
    }

    private Action1<InboxReputationDomain> saveToCache(final RequestParams requestParams) {
        return new Action1<InboxReputationDomain>() {
            @Override
            public void call(InboxReputationDomain inboxReputationDomain) {
                if (!inboxReputationDomain.getInboxReputation().isEmpty()
                        && isRequestNotFiltered(requestParams)) {
                    globalCacheManager.setKey(GetFirstTimeInboxReputationUseCase.CACHE_REPUTATION +
                            requestParams.getParameters().get(GetInboxReputationUseCase.PARAM_TAB))
                            .setCacheDuration(GetFirstTimeInboxReputationUseCase.DURATION_CACHE)
                            .setValue(CacheUtil.convertModelToString(inboxReputationDomain,
                                    new TypeToken<InboxReputationDomain>() {
                                    }.getType()))
                            .store();
                }
            }
        };
    }

    private boolean isRequestNotFiltered(RequestParams requestParams) {
        return requestParams.getBoolean(IS_SAVE_TO_CACHE,false);
    }
}
