package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class CacheApiGetCacheDataUseCase extends BaseApiCacheInterceptorUseCase<String> {

    public CacheApiGetCacheDataUseCase(CacheApiRepository cacheApiRepository) {
        super(cacheApiRepository);
    }

    @Override
    public Observable<String> createChildObservable(RequestParams requestParams) {
        return cacheApiRepository.isInWhiteList(cacheApiData.getHost(), cacheApiData.getPath()).filter(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean) {
                return aBoolean;
            }
        }).flatMap(new Func1<Boolean, Observable<String>>() {
            @Override
            public Observable<String> call(Boolean aBoolean) {
                return cacheApiRepository.getCachedResponse(cacheApiData.getHost(), cacheApiData.getPath(), cacheApiData.getRequestParam());
            }
        });
    }
}
