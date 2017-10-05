package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.model.CacheApiDataDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 8/24/17.
 */

public class CacheApiDataDeleteUseCase extends UseCase<Boolean> {

    public static final String DELETE_WHITELIST_DATA = "DELETE_WHITELIST_DATA";

    private ApiCacheRepository apiCacheRepository;

    @Inject
    public CacheApiDataDeleteUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread);
        this.apiCacheRepository = apiCacheRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        Object object = requestParams.getObject(DELETE_WHITELIST_DATA);
        return apiCacheRepository.singleDataDelete((CacheApiDataDomain) object);
    }

    public static RequestParams createParams(CacheApiDataDomain cacheApiDataDomainToDelete){
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(DELETE_WHITELIST_DATA, cacheApiDataDomainToDelete);
        return requestParams;
    }
}
