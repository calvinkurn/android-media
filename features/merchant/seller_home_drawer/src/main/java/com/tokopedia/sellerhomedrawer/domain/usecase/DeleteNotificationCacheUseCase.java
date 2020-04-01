package com.tokopedia.sellerhomedrawer.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.sellerhomedrawer.data.constant.SellerDrawerUrl;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;
import javax.inject.Named;

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
                SellerDrawerUrl.BASE_DOMAIN ,
                SellerDrawerUrl.User.PATH_NOTIFICATION + SellerDrawerUrl.User.PATH_GET_NOTIFICATION
        );
        return super.createObservable(newRequestParams);
    }
}