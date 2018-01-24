package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.data.source.db.model.CacheApiData;
import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by User on 8/30/2017.
 */

public abstract class BaseApiCacheInterceptorUseCase<E> extends UseCase<E> {

    public static final String PARAM_METHOD = "PARAM_METHOD";
    public static final String PARAM_HOST = "PARAM_HOST";
    public static final String PARAM_PATH = "PARAM_PATH";
    public static final String PARAM_REQUEST_PARAM = "PARAM_REQUEST_PARAM";

    protected final CacheApiRepository cacheApiRepository;
    protected CacheApiData cacheApiData;

    public BaseApiCacheInterceptorUseCase(CacheApiRepository cacheApiRepository) {
        this.cacheApiRepository = cacheApiRepository;
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