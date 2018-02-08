package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.domain.ApiCacheRepository;

import rx.Observable;

/**
 * Created by User on 8/30/2017.
 */

public abstract class BaseApiCacheInterceptorUseCase<E> extends UseCase<E> {

    public static final String PARAM_METHOD = "PARAM_METHOD";
    public static final String PARAM_HOST = "PARAM_HOST";
    public static final String PARAM_PATH = "PARAM_PATH";
    public static final String PARAM_REQUEST_PARAM = "PARAM_REQUEST_PARAM";

    protected final ApiCacheRepository apiCacheRepository;
    protected CacheApiData cacheApiData;

    public BaseApiCacheInterceptorUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread);
        this.apiCacheRepository = apiCacheRepository;
    }

    @Override
    public final Observable<E> createObservable(RequestParams requestParams) {
        String method = requestParams.getString(PARAM_METHOD, "");
        String host = requestParams.getString(PARAM_HOST, "");
        String path = requestParams.getString(PARAM_PATH, "");
        String requestParam = requestParams.getString(PARAM_REQUEST_PARAM, "");

        cacheApiData = new CacheApiData();
        cacheApiData.setMethod(method);
        cacheApiData.setHost(host);
        cacheApiData.setPath(path);
        cacheApiData.setRequestParam(requestParam);

        return createChildObservable(requestParams);
    }

    public abstract Observable<E> createChildObservable(RequestParams requestParams);
}