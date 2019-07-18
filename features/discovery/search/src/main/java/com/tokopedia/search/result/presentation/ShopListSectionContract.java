package com.tokopedia.search.result.presentation;

import com.tokopedia.search.result.presentation.model.ShopViewModel;

import java.util.List;
import java.util.Map;

public interface ShopListSectionContract {

    interface View extends SearchSectionContract.View {
        String getUserId();

        void onSearchShopSuccess(List<ShopViewModel.ShopItem> shopViewItemList, boolean isHasNextPage);

        void onSearchShopFailed();
    }

    interface Presenter extends SearchSectionContract.Presenter<View> {
        void loadShop(Map<String, Object> searchParameter);
    }
}
