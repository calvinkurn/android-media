package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.constant.CacheApiConstant;
import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.cacheapi.util.Injection;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class CacheApiGetCacheDataUseCase extends UseCase<String> {

    private CacheApiRepository cacheApiRepository;

    public CacheApiGetCacheDataUseCase() {
        cacheApiRepository = Injection.provideCacheApiRepository();
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        final String host = requestParams.getString(CacheApiConstant.PARAM_HOST, "");
        final String path = requestParams.getString(CacheApiConstant.PARAM_PATH, "");
        final String requestParam = requestParams.getString(CacheApiConstant.PARAM_REQUEST_PARAM, "");
        return cacheApiRepository.isInWhiteList(host, path).filter(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean) {
                return aBoolean;
            }
        }).flatMap(new Func1<Boolean, Observable<String>>() {
            @Override
            public Observable<String> call(Boolean aBoolean) {
                return cacheApiRepository.getCachedResponse(host, path, requestParam);
            }
        });
    }

    public static RequestParams createParams(String host, String path, String requestParam) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CacheApiConstant.PARAM_HOST, host);
        requestParams.putObject(CacheApiConstant.PARAM_PATH, path);
        requestParams.putObject(CacheApiConstant.PARAM_REQUEST_PARAM, requestParam);
        return requestParams;
    }
}
