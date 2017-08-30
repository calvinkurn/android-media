package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;

import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class CheckWhiteListUseCase extends UseCase<Boolean> {

    private ApiCacheInterceptorUseCase apiCacheInterceptorUseCase;

    public CheckWhiteListUseCase(ApiCacheInterceptorUseCase apiCacheInterceptorUseCase) {
        this.apiCacheInterceptorUseCase = apiCacheInterceptorUseCase;
    }

    public CheckWhiteListUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ApiCacheInterceptorUseCase apiCacheInterceptorUseCase) {
        super(threadExecutor, postExecutionThread);
        this.apiCacheInterceptorUseCase = apiCacheInterceptorUseCase;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        apiCacheInterceptorUseCase.createObservableSync(requestParams);
        return Observable.just(apiCacheInterceptorUseCase.isInWhiteList());
    }
}
