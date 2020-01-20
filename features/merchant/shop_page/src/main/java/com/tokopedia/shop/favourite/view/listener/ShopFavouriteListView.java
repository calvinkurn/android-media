package com.tokopedia.shop.favourite.view.listener;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.favourite.view.model.ShopFollowerUiModel;

public interface ShopFavouriteListView extends BaseListViewListener<ShopFollowerUiModel> {

    void onSuccessGetShopInfo(ShopInfo shopInfo);

    void onErrorToggleFavourite(Throwable throwable);

    void onSuccessToggleFavourite(boolean successValue);
}
