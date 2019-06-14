package com.tokopedia.gm.common.domain.interactor;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

public class DeleteFeatureProductListCacheUseCase extends CacheApiDataDeleteUseCase {

    @Inject
    public DeleteFeatureProductListCacheUseCase(@ApplicationContext Context context){
        super(context);
    }

    public Observable<Boolean> createObservable() {
        return createObservable(RequestParams.create());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        RequestParams newRequestParams = CacheApiDataDeleteUseCase
                .createParams(GMCommonUrl.BASE_URL, GMCommonUrl.FEATURED_PRODUCT_URL, true);
        return super.createObservable(newRequestParams);
    }
}
