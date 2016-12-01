package com.tokopedia.core.shopinfo.interactor;

import com.tokopedia.core.shopinfo.models.shopfavoritedmodel.ShopFavoritedResponse;

/**
 * Created by Alfia on 10/6/2016.
 */
public interface ShopFavoritedCacheInteractor {
    void getShopFavoritedCache(GetShopFavoritedCacheListener listener);

    void setShopFavoritedCache(ShopFavoritedResponse result);

    interface GetShopFavoritedCacheListener {
        void onSuccess(ShopFavoritedResponse result);

        void onError(Throwable e);
    }
}
