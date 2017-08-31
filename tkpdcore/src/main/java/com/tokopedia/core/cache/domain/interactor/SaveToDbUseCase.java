package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.repository.ApiCacheRepositoryImpl;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.domain.ApiCacheRepository;

import okhttp3.Response;
import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class SaveToDbUseCase extends BaseApiCacheInterceptorUseCase<Boolean> {

    public static final String RESPONSE = "RESPONSE";

    public SaveToDbUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread, apiCacheRepository);
    }

    public SaveToDbUseCase(ApiCacheRepositoryImpl apiCacheRepository) {
        super(apiCacheRepository);
    }

    @Override
    public Observable<Boolean> createChildObservable(RequestParams requestParams) {
        Response response = (Response) requestParams.getObject(RESPONSE);
        CacheApiWhitelist inWhiteListRaw = apiCacheRepository.isInWhiteListRaw(paramsCacheApiData.getHost(), paramsCacheApiData.getPath());
        if (inWhiteListRaw != null) {
            apiCacheRepository.updateResponse(paramsCacheApiData, inWhiteListRaw, response);
        }

        return Observable.just(true);
    }
}
