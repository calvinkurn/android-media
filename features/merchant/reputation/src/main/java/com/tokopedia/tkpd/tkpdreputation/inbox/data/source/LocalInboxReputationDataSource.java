package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author by nisie on 9/20/17.
 */

public class LocalInboxReputationDataSource {
    private final PersistentCacheManager persistentCacheManager;

    public LocalInboxReputationDataSource(PersistentCacheManager persistentCacheManager) {
        this.persistentCacheManager = persistentCacheManager;
    }

    public Observable<InboxReputationDomain> getInboxReputationFromCache(RequestParams requestParams) {
        return Observable.just(GetFirstTimeInboxReputationUseCase.CACHE_REPUTATION +
                requestParams.getParameters().get(GetInboxReputationUseCase.PARAM_TAB))
                .map(key -> {
                    if (getCache(key) != null)
                        return CacheUtil.convertStringToModel(getCache(key),
                                new TypeToken<InboxReputationDomain>() {
                                }.getType());
                    else throw new RuntimeException("NO CACHE");
                });
    }

    private String getCache(String key) {
        return persistentCacheManager.getString(key);
    }
}
