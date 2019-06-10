package com.tokopedia.shop.product.domain.interactor;

import android.content.Context;

import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/7/17.
 */

public class DeleteShopProductTomeUseCase extends CacheApiDataDeleteUseCase {

    public DeleteShopProductTomeUseCase(Context context) {
        super(context);
    }

    public Observable<Boolean> createObservable() {
        return createObservable(RequestParams.create());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        RequestParams newRequestParams = CacheApiDataDeleteUseCase.createParams(ShopUrl.BASE_URL, ShopUrl.SHOP_PRODUCT_PATH);
        return super.createObservable(newRequestParams);
    }
}