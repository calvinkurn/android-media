package com.tokopedia.core.shopinfo.interactor;

import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;

/**
 * Created by brillint.oka on 26/04/17.
 */

public interface ShopProductCacheInteractor {
    void getAllShopProductCache(GetShopProductCacheListener listener);

    void getLimitedShopProductCache(int limit, GetShopProductCacheListener listener);

    void setShopProductCache(ProductModel model);

    void deleteShopProductCache();

    interface GetShopProductCacheListener {
        void onSuccess(ProductModel model);

        void onError(Throwable e);
    }
}
