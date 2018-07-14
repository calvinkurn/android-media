package com.tokopedia.shop.product.view.listener;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;
@Deprecated
public interface ShopProductListView extends BaseListViewListener<ShopProductViewModelOld> {

    void onSuccessAddToWishList(String productId, Boolean value);

    void onErrorAddToWishList(Throwable e);

    void onSuccessRemoveFromWishList(String productId, Boolean value);

    void onErrorRemoveFromWishList(Throwable e);

    void onSuccessGetShopName(ShopInfo shopName);

    void onSuccessGetEtalase(String etalaseId, String etalaseName);

}
