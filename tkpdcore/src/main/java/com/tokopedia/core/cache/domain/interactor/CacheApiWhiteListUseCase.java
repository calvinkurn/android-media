package com.tokopedia.core.cache.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 8/16/17.
 */

public class CacheApiWhiteListUseCase extends UseCase<Boolean> {

    public static final String ADD_WHITELIST_COLLECTIONS = "ADD_WHITELIST_COLLECTIONS";

    private ApiCacheRepository apiCacheRepository;

    @Inject
    public CacheApiWhiteListUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread);
        this.apiCacheRepository = apiCacheRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        Object object = requestParams.getObject(ADD_WHITELIST_COLLECTIONS);
        return apiCacheRepository.insertWhiteList((Collection<CacheApiWhiteListDomain>) object);
    }
}
