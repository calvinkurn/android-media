package com.tokopedia.cacheapi.domain.interactor;

import android.content.Context;

import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.cacheapi.util.Injection;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by normansyahputa on 8/24/17.
 */

public class CacheApiClearAllUseCase extends UseCase<Boolean> {

    private CacheApiRepository cacheApiRepository;

    public CacheApiClearAllUseCase(Context context) {
        cacheApiRepository = Injection.provideCacheApiRepository(context.getApplicationContext());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return cacheApiRepository.deleteAllCacheData();
    }
}

