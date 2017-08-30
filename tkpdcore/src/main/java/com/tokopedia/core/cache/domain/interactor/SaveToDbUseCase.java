package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;

import okhttp3.Response;
import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class SaveToDbUseCase extends UseCase<Boolean> {

    public static final String RESPONSE = "RESPONSE";
    private ApiCacheInterceptorUseCase apiCacheInterceptorUseCase;

    public SaveToDbUseCase(ApiCacheInterceptorUseCase apiCacheInterceptorUseCase) {
        this.apiCacheInterceptorUseCase = apiCacheInterceptorUseCase;
    }

    public SaveToDbUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ApiCacheInterceptorUseCase apiCacheInterceptorUseCase) {
        super(threadExecutor, postExecutionThread);
        this.apiCacheInterceptorUseCase = apiCacheInterceptorUseCase;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        Response response = (Response) requestParams.getObject(RESPONSE);

        apiCacheInterceptorUseCase.updateResponse(response);

        return Observable.just(true);
    }
}
