package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class CacheApiCheckWhiteListUseCase extends BaseApiCacheInterceptorUseCase<Boolean> {

    public CacheApiCheckWhiteListUseCase(CacheApiRepository cacheApiRepository) {
        super(cacheApiRepository);
    }

    @Override
    public Observable<Boolean> createChildObservable(RequestParams requestParams) {
        return cacheApiRepository.isInWhiteList(cacheApiData.getHost(), cacheApiData.getPath());
    }
}
