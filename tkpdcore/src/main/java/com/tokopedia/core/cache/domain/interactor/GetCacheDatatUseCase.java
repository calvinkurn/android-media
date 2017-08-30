package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.source.db.CacheApiData;

import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class GetCacheDatatUseCase extends UseCase<String> {

    private ApiCacheInterceptorUseCase apiCacheInterceptorUseCase;

    public GetCacheDatatUseCase(ApiCacheInterceptorUseCase apiCacheInterceptorUseCase) {
        this.apiCacheInterceptorUseCase = apiCacheInterceptorUseCase;
    }

    public GetCacheDatatUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ApiCacheInterceptorUseCase apiCacheInterceptorUseCase) {
        super(threadExecutor, postExecutionThread);
        this.apiCacheInterceptorUseCase = apiCacheInterceptorUseCase;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        CacheApiData cacheApiData = apiCacheInterceptorUseCase.createObservableSync(requestParams).toBlocking().first();
        return Observable.just(cacheApiData.getResponseBody());
    }
}
