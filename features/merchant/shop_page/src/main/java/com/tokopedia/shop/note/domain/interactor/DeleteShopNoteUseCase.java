package com.tokopedia.shop.note.domain.interactor;

import android.content.Context;

import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/7/17.
 */

public class DeleteShopNoteUseCase extends CacheApiDataDeleteUseCase {

    public DeleteShopNoteUseCase(Context context) {
        super(context);
    }

    public Observable<Boolean> createObservable() {
        return createObservable(RequestParams.create());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        RequestParams newRequestParams = CacheApiDataDeleteUseCase.createParams(ShopUrl.BASE_URL, ShopUrl.SHOP_NOTE_PATH);
        return super.createObservable(newRequestParams);
    }
}