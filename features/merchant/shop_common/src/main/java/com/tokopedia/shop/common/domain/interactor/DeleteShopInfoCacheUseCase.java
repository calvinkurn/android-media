package com.tokopedia.shop.common.domain.interactor;

import android.content.Context;

import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/7/17.
 */

public class DeleteShopInfoCacheUseCase extends CacheApiDataDeleteUseCase {

    public DeleteShopInfoCacheUseCase(Context context) {
        super(context);
    }

    public Observable<Boolean> createObservable() {
        return createObservable(RequestParams.create());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        RequestParams newRequestParams = CacheApiDataDeleteUseCase.createParams(ShopCommonUrl.BASE_URL, ShopCommonUrl.SHOP_INFO_PATH);
        return super.createObservable(newRequestParams);
    }
}