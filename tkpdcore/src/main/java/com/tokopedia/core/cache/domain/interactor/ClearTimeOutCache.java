package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.repository.ApiCacheRepositoryImpl;
import com.tokopedia.core.cache.domain.ApiCacheRepository;

import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class ClearTimeOutCache extends BaseApiCacheInterceptorUseCase<Boolean> {

    public ClearTimeOutCache(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread, apiCacheRepository);
    }

    public ClearTimeOutCache(ApiCacheRepositoryImpl apiCacheRepository) {
        super(apiCacheRepository);
    }

    @Override
    public Observable<Boolean> createChildObservable(RequestParams requestParams) {
        return apiCacheRepository.clearTimeout();
    }
}
