package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.utils.ReputationUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private final UserSessionInterface userSessionInterface;

    public CloudInboxReputationDataSource(ReputationService reputationService,
                                          InboxReputationMapper inboxReputationMapper,
                                          GlobalCacheManager globalCacheManager,
                                          UserSessionInterface userSessionInterface) {
        this.userSessionInterface = userSessionInterface;
        this.reputationService = reputationService;
        this.inboxReputationMapper = inboxReputationMapper;
        this.globalCacheManager = globalCacheManager;
    }


    public Observable<InboxReputationDomain> getInboxReputation(com.tokopedia.usecase.RequestParams requestParams) {
        return reputationService.getApi().getInbox(
                AuthHelper.generateParamsNetwork(
                        userSessionInterface.getUserId(),
                        userSessionInterface.getDeviceId(),
                        ReputationUtil.convertMapObjectToString(requestParams.getParameters())))
                .map(inboxReputationMapper)
                .doOnNext(saveToCache(requestParams));
    }

    private Action1<InboxReputationDomain> saveToCache(final RequestParams requestParams) {
        return inboxReputationDomain -> {
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
        };
    }

    private boolean isRequestNotFiltered(RequestParams requestParams) {
        return requestParams.getBoolean(IS_SAVE_TO_CACHE,false);
    }
}
