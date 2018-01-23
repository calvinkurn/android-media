package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.domain.ApiCacheRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by normansyahputa on 8/24/17.
 */

public class CacheApiClearAllUseCase extends UseCase<Boolean> {

    private ApiCacheRepository apiCacheRepository;

    public CacheApiClearAllUseCase(ApiCacheRepository apiCacheRepository) {
        this.apiCacheRepository = apiCacheRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return apiCacheRepository.deleteAllCacheData();
    }
}

