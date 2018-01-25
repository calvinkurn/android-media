package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.cacheapi.util.Injection;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Collection;

import rx.Observable;

/**
 * Created by normansyahputa on 8/16/17.
 */

public class CacheApiWhiteListUseCase extends UseCase<Boolean> {

    public static final String ADD_WHITELIST_COLLECTIONS = "ADD_WHITELIST_COLLECTIONS";
    public static final String APP_VERSION_NAME = "APP_VERSION_NAME";

    private CacheApiRepository apiCacheRepository;

    public CacheApiWhiteListUseCase() {
        this(Injection.provideCacheApiRepository());
    }

    public CacheApiWhiteListUseCase(CacheApiRepository cacheApiRepository) {
        this.apiCacheRepository = cacheApiRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        Object object = requestParams.getObject(ADD_WHITELIST_COLLECTIONS);
        String versionName = requestParams.getString(APP_VERSION_NAME, "");
        return apiCacheRepository.insertWhiteList((Collection<CacheApiWhiteListDomain>) object, versionName);
    }
}
