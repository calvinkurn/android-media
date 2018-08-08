package com.tokopedia.shop.product.view.listener;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

public interface ShopProductListView extends BaseListViewListener<ShopProductViewModel> {

    void onUserNotLoginError(Throwable e);

    void onSuccessGetShopName(ShopInfo shopName);

    void onSuccessGetEtalase(String etalaseId, String etalaseName);

}
