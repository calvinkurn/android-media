package com.tokopedia.shop.sort.view.listener;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.sort.view.model.ShopProductSortModel;

public interface ShopProductSortListView extends BaseListViewListener<ShopProductSortModel> {

    void onSuccessGetShopInfo(ShopInfo shopInfo);
}
