package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.repository.ApiCacheRepositoryImpl;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.domain.ApiCacheRepository;

import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class GetCacheDataUseCaseUseCase extends BaseApiCacheInterceptorUseCase<String> {

    public GetCacheDataUseCaseUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread, apiCacheRepository);
    }

    public GetCacheDataUseCaseUseCase(ApiCacheRepositoryImpl apiCacheRepository) {
        super(apiCacheRepository);
    }


    @Override
    public Observable<String> createChildObservable(RequestParams requestParams) {
        if (apiCacheRepository.isInWhiteList(paramsCacheApiData.getHost(), paramsCacheApiData.getPath())) {
            CacheApiData tempData = apiCacheRepository.queryDataFrom(paramsCacheApiData.getHost(), paramsCacheApiData.getPath(), paramsCacheApiData.getRequestParam());

            if (tempData != null) {

                paramsCacheApiData.setResponseDate(tempData.responseDate);
                paramsCacheApiData.setExpiredDate(tempData.expiredDate);
                paramsCacheApiData.setResponseBody(tempData.responseBody);
            }
        }
        return Observable.just(paramsCacheApiData.getResponseBody());
    }
}
