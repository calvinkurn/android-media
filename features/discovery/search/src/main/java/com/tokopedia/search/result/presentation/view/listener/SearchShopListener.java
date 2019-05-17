package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.search.result.presentation.model.ShopViewModel;

import java.util.List;

public interface SearchShopListener {
    void onSearchShopSuccess(List<ShopViewModel.ShopViewItem> shopViewItemList, boolean isHasNextPage);

    void onSearchShopFailed();

    void getDynamicFilter();
}
