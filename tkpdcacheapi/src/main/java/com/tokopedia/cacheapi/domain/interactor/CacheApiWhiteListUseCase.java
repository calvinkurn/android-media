package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.domain.ApiCacheRepository;
import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Collection;

import rx.Observable;

/**
 * Created by normansyahputa on 8/16/17.
 */

public class CacheApiWhiteListUseCase extends UseCase<Boolean> {

    public static final String ADD_WHITELIST_COLLECTIONS = "ADD_WHITELIST_COLLECTIONS";

    private ApiCacheRepository apiCacheRepository;

    public CacheApiWhiteListUseCase(ApiCacheRepository apiCacheRepository) {
        this.apiCacheRepository = apiCacheRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        Object object = requestParams.getObject(ADD_WHITELIST_COLLECTIONS);
        return apiCacheRepository.insertWhiteList((Collection<CacheApiWhiteListDomain>) object);
    }
}
