package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.search.result.presentation.model.ShopViewModel;

import java.util.List;

public interface SearchShopListener {
    void onSuccess(List<ShopViewModel.ShopViewItem> shopItemList, boolean isHasNextPage);

    void onFailed();

    void getDynamicFilter();
}
