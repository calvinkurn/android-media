package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.domain.ApiCacheRepository;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class GetCacheDataUseCaseUseCase extends BaseApiCacheInterceptorUseCase<String> {

    public GetCacheDataUseCaseUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread, apiCacheRepository);
    }

    @Override
    public Observable<String> createChildObservable(RequestParams requestParams) {
        return apiCacheRepository.isInWhiteList(cacheApiData.getHost(), cacheApiData.getPath()).filter(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean) {
                return aBoolean;
            }
        }).flatMap(new Func1<Boolean, Observable<CacheApiData>>() {
            @Override
            public Observable<CacheApiData> call(Boolean aBoolean) {
                return apiCacheRepository.queryDataFrom(cacheApiData.getHost(), cacheApiData.getPath(), cacheApiData.getRequestParam());
            }
        }).map(new Func1<CacheApiData, String>() {
            @Override
            public String call(CacheApiData cacheApiData) {
                if (cacheApiData == null) {
                    return null;
                }
                return cacheApiData.getResponseBody();
            }
        });
    }
}
