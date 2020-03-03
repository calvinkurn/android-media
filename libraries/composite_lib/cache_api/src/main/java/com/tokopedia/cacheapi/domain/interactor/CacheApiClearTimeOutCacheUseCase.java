package com.tokopedia.cacheapi.domain.interactor;

import android.content.Context;

import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.cacheapi.util.Injection;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class CacheApiClearTimeOutCacheUseCase extends UseCase<Boolean> {

    private CacheApiRepository cacheApiRepository;

    public CacheApiClearTimeOutCacheUseCase(Context context) {
        cacheApiRepository = Injection.provideCacheApiRepository(context.getApplicationContext());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return cacheApiRepository.deleteExpiredCachedData();
    }
}
