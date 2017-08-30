package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.repository.ApiCacheRepositoryImpl;
import com.tokopedia.core.cache.domain.ApiCacheRepository;

import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class CheckWhiteListUseCase extends BaseApiCacheInterceptor<Boolean> {

    public CheckWhiteListUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread, apiCacheRepository);
    }

    @Override
    public Observable<Boolean> createChildObservable(RequestParams requestParams) {
        return Observable.just(apiCacheRepository.isInWhiteList(cacheApiData.getHost(), cacheApiData.getPath()));
    }
}
