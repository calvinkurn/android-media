package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 9/20/17.
 */

public class LocalInboxReputationDataSource {
    private final GlobalCacheManager globalCacheManager;

    public LocalInboxReputationDataSource(GlobalCacheManager globalCacheManager) {
        this.globalCacheManager = globalCacheManager;
    }

    public Observable<InboxReputationDomain> getInboxReputationFromCache(RequestParams requestParams) {
        return Observable.just(GetFirstTimeInboxReputationUseCase.CACHE_REPUTATION +
                requestParams.getParameters().get(GetInboxReputationUseCase.PARAM_TAB))
                .map(new Func1<String, InboxReputationDomain>() {
                    @Override
                    public InboxReputationDomain call(String key) {
                        if (getCache(key) != null)
                            return CacheUtil.convertStringToModel(getCache(key),
                                    new TypeToken<InboxReputationDomain>() {
                                    }.getType());
                        else throw new RuntimeException("NO CACHE");
                    }
                });
    }

    private String getCache(String key) {
        return globalCacheManager.getValueString(key);
    }
}
