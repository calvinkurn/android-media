package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.domain.ApiCacheRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 8/24/17.
 */

public class CacheApiClearAllUseCase extends UseCase<Boolean> {

    private ApiCacheRepository apiCacheRepository;

    @Inject
    public CacheApiClearAllUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread);
        this.apiCacheRepository = apiCacheRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return apiCacheRepository.deleteAllCacheData();
    }
}

