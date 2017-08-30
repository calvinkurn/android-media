package com.tokopedia.core.cache.domain.interactor;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.repository.ApiCacheRepositoryImpl;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiData_Table;
import com.tokopedia.core.cache.domain.ApiCacheRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class ClearTimeOutCacheDataUseCase extends UseCase<Boolean> {

    public ClearTimeOutCacheData(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread, apiCacheRepository);
    }

    public ClearTimeOutCacheData(ApiCacheRepositoryImpl apiCacheRepository) {
        super(apiCacheRepository);
    }
    @Override
    public Observable<Boolean> createChildObservable(RequestParams requestParams) {
        apiCacheRepository.clearTimeout();
        return Observable.just(true);
    }
}
