package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.constant.CacheApiConstant;
import com.tokopedia.cacheapi.data.source.db.model.CacheApiWhitelist;
import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.cacheapi.util.Injection;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class CacheApiSaveToDbUseCase extends UseCase<Boolean> {

    private CacheApiRepository cacheApiRepository;

    public CacheApiSaveToDbUseCase() {
        cacheApiRepository = Injection.provideCacheApiRepository();
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        String host = requestParams.getString(CacheApiConstant.PARAM_HOST, "");
        String path = requestParams.getString(CacheApiConstant.PARAM_PATH, "");
        final Response response = (Response) requestParams.getObject(CacheApiConstant.RESPONSE);
        return cacheApiRepository.getWhiteList(host, path).flatMap(new Func1<CacheApiWhitelist, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(CacheApiWhitelist cacheApiWhitelist) {
                if (cacheApiWhitelist != null) {
                    return cacheApiRepository.updateResponse(response, (int) cacheApiWhitelist.getExpiredTime());
                } else {
                    return Observable.just(false);
                }
            }
        });
    }

    public static RequestParams createParams(String host, String path, Response response) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CacheApiConstant.PARAM_HOST, host);
        requestParams.putObject(CacheApiConstant.PARAM_PATH, path);
        return requestParams;
    }
}