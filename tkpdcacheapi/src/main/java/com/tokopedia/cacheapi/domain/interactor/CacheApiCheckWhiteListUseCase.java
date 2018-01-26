package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.constant.CacheApiConstant;
import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.cacheapi.util.Injection;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class CacheApiCheckWhiteListUseCase extends UseCase<Boolean> {

    private CacheApiRepository cacheApiRepository;

    public CacheApiCheckWhiteListUseCase() {
        cacheApiRepository = Injection.provideCacheApiRepository();
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        String host = requestParams.getString(CacheApiConstant.PARAM_HOST, "");
        String path = requestParams.getString(CacheApiConstant.PARAM_PATH, "");
        return cacheApiRepository.isInWhiteList(host, path);
    }

    public static RequestParams createParams(String host, String path) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CacheApiConstant.PARAM_HOST, host);
        requestParams.putObject(CacheApiConstant.PARAM_PATH, path);
        return requestParams;
    }
}
