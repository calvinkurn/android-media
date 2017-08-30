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

public class GetCacheDataUseCase extends BaseApiCacheInterceptor<String> {

    private CacheApiData tempData;
    private boolean isEmptyData;

    public GetCacheDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread, apiCacheRepository);
    }

    public GetCacheDataUseCase(ApiCacheRepositoryImpl apiCacheRepository) {
        super(apiCacheRepository);
    }


    @Override
    public Observable<String> createChildObservable(RequestParams requestParams) {
        if (apiCacheRepository.isInWhiteList(cacheApiData.getHost(), cacheApiData.getPath())) {
            tempData = apiCacheRepository.queryDataFrom(cacheApiData.getHost(), cacheApiData.getPath(), cacheApiData.getRequestParam());
            isEmptyData = (tempData == null);

            if (!isEmptyData) {

                cacheApiData.setResponseDate(tempData.responseDate);
                cacheApiData.setExpiredDate(tempData.expiredDate);
                cacheApiData.setResponseBody(tempData.responseBody);
            }
        }
        return Observable.just(cacheApiData.getResponseBody());
    }
}
