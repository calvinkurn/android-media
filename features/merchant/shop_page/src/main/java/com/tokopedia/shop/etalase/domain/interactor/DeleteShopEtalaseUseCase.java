package com.tokopedia.shop.etalase.domain.interactor;

import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/7/17.
 */

public class DeleteShopEtalaseUseCase extends CacheApiDataDeleteUseCase {

    public Observable<Boolean> createObservable() {
        return createObservable(RequestParams.create());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        RequestParams newRequestParams = CacheApiDataDeleteUseCase.createParams(ShopUrl.BASE_WS_URL, ShopUrl.SHOP_ETALASE);
        return super.createObservable(newRequestParams);
    }
}