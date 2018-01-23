package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.domain.ApiCacheRepository;
import com.tokopedia.cacheapi.util.CacheApiUtils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by normansyahputa on 8/24/17.
 */

public class CacheApiDataDeleteUseCase extends UseCase<Boolean> {

    private static final String PARAM_DOMAIN = "PARAM_DOMAIN";
    private static final String PARAM_PATH = "PARAM_PATH";

    private ApiCacheRepository apiCacheRepository;

    public CacheApiDataDeleteUseCase(ApiCacheRepository apiCacheRepository) {
        this.apiCacheRepository = apiCacheRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        String domain = requestParams.getString(PARAM_DOMAIN, "");
        String path = requestParams.getString(PARAM_PATH, "");
        return apiCacheRepository.deleteCachedData(domain, path);
    }

    public static RequestParams createParams(String url) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_DOMAIN, CacheApiUtils.getHost(url));
        requestParams.putObject(PARAM_PATH, CacheApiUtils.getPath(url));
        return requestParams;
    }

    public static RequestParams createParams(String host, String path) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_DOMAIN, host);
        requestParams.putObject(PARAM_PATH, path);
        return requestParams;
    }
}