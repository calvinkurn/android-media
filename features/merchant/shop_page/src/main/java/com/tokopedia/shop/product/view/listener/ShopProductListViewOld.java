package com.tokopedia.shop.product.view.listener;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

@Deprecated
public interface ShopProductListViewOld extends BaseListViewListener<ShopProductViewModelOld> {

    void onSuccessAddToWishList(String productId, Boolean value);

    void onErrorAddToWishList(Throwable e);

    void onSuccessRemoveFromWishList(String productId, Boolean value);

    void onErrorRemoveFromWishList(Throwable e);

    void onSuccessGetShopInfo(ShopInfo shopName);

    void onSuccessGetEtalase(String etalaseId, String etalaseName);

}
