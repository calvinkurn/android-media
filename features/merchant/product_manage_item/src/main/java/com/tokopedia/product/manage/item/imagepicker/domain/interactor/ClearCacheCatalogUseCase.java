package com.tokopedia.product.manage.item.imagepicker.domain.interactor;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.product.manage.item.imagepicker.util.CatalogConstant;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 6/8/18.
 */

public class ClearCacheCatalogUseCase extends CacheApiDataDeleteUseCase{

    @Inject
    public ClearCacheCatalogUseCase(@ApplicationContext Context context) {
        super(context);
    }

    public Observable<Boolean> createObservable() {
        return createObservable(RequestParams.create());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        RequestParams newRequestParams = CacheApiDataDeleteUseCase.createParams(CatalogConstant.URL_HADES, CatalogConstant.URL_GET_CATALOG_IMAGE);
        return super.createObservable(newRequestParams);
    }
}
