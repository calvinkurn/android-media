package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.domain.model.result.ProductSavedResult;
import com.tokopedia.posapp.domain.model.shop.ShopDomain;
import com.tokopedia.posapp.domain.model.shop.ShopProductListDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/3/17.
 */

public interface ShopRepository {
    Observable<ShopDomain> getShop(RequestParams requestParams);

    Observable<ShopProductListDomain> getShopProduct(RequestParams requestParams);

    Observable<ProductSavedResult> storeProductToCache(ShopProductListDomain productModel);
}
