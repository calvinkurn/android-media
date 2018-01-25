package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.cacheapi.util.Injection;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class CacheApiClearTimeOutCacheUseCase extends UseCase<Boolean> {

    private CacheApiRepository apiCacheRepository;

    public CacheApiClearTimeOutCacheUseCase() {
        this(Injection.provideCacheApiRepository());
    }

    public CacheApiClearTimeOutCacheUseCase(CacheApiRepository cacheApiRepository) {
        this.apiCacheRepository = cacheApiRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return apiCacheRepository.deleteExpiredCachedData();
    }
}
