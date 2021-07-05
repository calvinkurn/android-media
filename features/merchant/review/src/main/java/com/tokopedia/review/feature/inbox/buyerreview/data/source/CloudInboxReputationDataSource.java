package com.tokopedia.review.feature.inbox.buyerreview.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.review.common.util.ReviewUtil;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationMapper;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetInboxReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 8/14/17.
 */

public class CloudInboxReputationDataSource {
    public static final String IS_SAVE_TO_CACHE = "IS_SAVE_TO_CACHE";
    private final InboxReputationMapper inboxReputationMapper;
    private final PersistentCacheManager persistentCacheManager;
    private final ReputationService reputationService;
    private final UserSessionInterface userSessionInterface;

    public CloudInboxReputationDataSource(ReputationService reputationService,
                                          InboxReputationMapper inboxReputationMapper,
                                          PersistentCacheManager persistentCacheManager,
                                          UserSessionInterface userSessionInterface) {
        this.userSessionInterface = userSessionInterface;
        this.reputationService = reputationService;
        this.inboxReputationMapper = inboxReputationMapper;
        this.persistentCacheManager = persistentCacheManager;
    }


    public Observable<InboxReputationDomain> getInboxReputation(RequestParams requestParams) {
        return reputationService.getApi().getInbox(
                AuthHelper.generateParamsNetwork(
                        userSessionInterface.getUserId(),
                        userSessionInterface.getDeviceId(),
                        ReviewUtil.INSTANCE.convertMapObjectToString(requestParams.getParameters())))
                .map(inboxReputationMapper)
                .doOnNext(saveToCache(requestParams));
    }

    private Action1<InboxReputationDomain> saveToCache(final RequestParams requestParams) {
        return inboxReputationDomain -> {
            if (!inboxReputationDomain.getInboxReputation().isEmpty()
                    && isRequestNotFiltered(requestParams)) {

                String key = GetFirstTimeInboxReputationUseCase.CACHE_REPUTATION + requestParams.getParameters().get(GetInboxReputationUseCase.PARAM_TAB);
                String value = CacheUtil.convertModelToString(inboxReputationDomain,
                        new TypeToken<InboxReputationDomain>() {
                        }.getType());
                int cacheDuration = GetFirstTimeInboxReputationUseCase.DURATION_CACHE;
                persistentCacheManager.put(
                        key,
                        value,
                        cacheDuration
                );
            }
        };
    }

    private boolean isRequestNotFiltered(RequestParams requestParams) {
        return requestParams.getBoolean(IS_SAVE_TO_CACHE,false);
    }
}
