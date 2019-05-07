package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;

import java.util.List;

public interface ShopListLoadMoreListener {

    void onSuccess(List<ShopViewModel.ShopItem> shopItemList, boolean isHasNextPage);
    void onFailed();
}
