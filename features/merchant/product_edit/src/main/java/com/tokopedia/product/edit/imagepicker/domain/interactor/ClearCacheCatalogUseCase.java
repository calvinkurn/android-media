package com.tokopedia.product.edit.imagepicker.domain.interactor;

import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.product.edit.imagepicker.util.CatalogConstant;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 6/8/18.
 */

public class ClearCacheCatalogUseCase extends CacheApiDataDeleteUseCase{

    @Inject
    public ClearCacheCatalogUseCase() {
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
