package com.tokopedia.core.shopinfo.interactor;

import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;

/**
 * Created by nakama on 26/04/17.
 */

public interface ShopProductCacheInteractor {
    void getShopProductCache(GetShopProductCacheListener listener);

    void setShopProductCache(ProductModel model);

    void deleteShopProductCache();

    interface GetShopProductCacheListener {
        void onSuccess(ProductModel model);

        void onError(Throwable e);
    }
}
