package com.tokopedia.core.drawer2.domain.interactor;

import android.content.Context;

import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

public class DeleteNotificationCacheUseCase extends CacheApiDataDeleteUseCase {

    @Inject
    public DeleteNotificationCacheUseCase(@ApplicationContext Context context) {
        super(context);
    }

    public Observable<Boolean> createObservable() {
        return createObservable(RequestParams.create());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        RequestParams newRequestParams = CacheApiDataDeleteUseCase.createParams(
                TkpdBaseURL.BASE_DOMAIN ,
                TkpdBaseURL.User.PATH_NOTIFICATION + TkpdBaseURL.User.PATH_GET_NOTIFICATION
        );
        return super.createObservable(newRequestParams);
    }
}