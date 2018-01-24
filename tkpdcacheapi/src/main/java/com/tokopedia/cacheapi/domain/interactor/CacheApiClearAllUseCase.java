package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by normansyahputa on 8/24/17.
 */

public class CacheApiClearAllUseCase extends UseCase<Boolean> {

    private CacheApiRepository apiCacheRepository;

    public CacheApiClearAllUseCase(CacheApiRepository cacheApiRepository) {
        this.apiCacheRepository = cacheApiRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return apiCacheRepository.deleteAllCacheData();
    }
}

