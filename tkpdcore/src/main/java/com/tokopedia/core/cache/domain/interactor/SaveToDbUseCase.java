package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
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

public class SaveToDbUseCase extends BaseApiCacheInterceptor<Boolean> {

    public static final String RESPONSE = "RESPONSE";

    public SaveToDbUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread, apiCacheRepository);
    }

    public SaveToDbUseCase(ApiCacheRepositoryImpl apiCacheRepository) {
        super(apiCacheRepository);
    }


    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        Response response = (Response) requestParams.getObject(RESPONSE);

        super.createObservable(requestParams);
        CacheApiWhitelist inWhiteListRaw = apiCacheRepository.isInWhiteListRaw(cacheApiData.getHost(), cacheApiData.getHost());
        if(inWhiteListRaw != null){
            apiCacheRepository.updateResponse(cacheApiData, inWhiteListRaw, response);
        }

        return Observable.just(true);
    }
}
