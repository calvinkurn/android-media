package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.domain.ApiCacheRepository;

import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class SaveToDbUseCase extends BaseApiCacheInterceptorUseCase<Boolean> {

    public static final String RESPONSE = "RESPONSE";

    public SaveToDbUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread, apiCacheRepository);
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