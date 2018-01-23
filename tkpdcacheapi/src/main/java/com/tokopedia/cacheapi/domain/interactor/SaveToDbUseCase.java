package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.data.source.db.CacheApiWhitelist;
import com.tokopedia.cacheapi.domain.ApiCacheRepository;
import com.tokopedia.usecase.RequestParams;

import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class SaveToDbUseCase extends BaseApiCacheInterceptorUseCase<Boolean> {

    public static final String RESPONSE = "RESPONSE";

    public SaveToDbUseCase(ApiCacheRepository apiCacheRepository) {
        super(apiCacheRepository);
    }

    @Override
    public Observable<Boolean> createChildObservable(RequestParams requestParams) {
        final Response response = (Response) requestParams.getObject(RESPONSE);
        return apiCacheRepository.getWhiteList(cacheApiData.getHost(), cacheApiData.getPath()).flatMap(new Func1<CacheApiWhitelist, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(CacheApiWhitelist cacheApiWhitelist) {
                if (cacheApiWhitelist != null) {
                    return apiCacheRepository.updateResponse(response, (int) cacheApiWhitelist.getExpiredTime());
                } else {
                    return Observable.just(false);
                }
            }
        });
    }
}