package com.tokopedia.search.result.presentation;

import com.tokopedia.search.result.presentation.model.ShopViewModel;
import com.tokopedia.search.result.presentation.view.listener.FavoriteActionListener;
import com.tokopedia.search.result.presentation.view.listener.RequestDynamicFilterListener;
import com.tokopedia.search.result.presentation.view.listener.SearchShopListener;

import java.util.List;
import java.util.Map;

public interface ShopListSectionContract {

    interface View extends SearchSectionContract.View {
        boolean isUserHasLogin();

        String getUserId();

        void disableFavoriteButton(int adapterPosition);

        void enableFavoriteButton(int adapterPosition);

        void onSearchShopSuccess(List<ShopViewModel.ShopViewItem> shopViewItemList, boolean isHasNextPage);

        void onSearchShopFailed();
    }

    interface Presenter extends SearchSectionContract.Presenter<View> {
        void setFavoriteActionListener(FavoriteActionListener favoriteActionListener);

        void loadShop(Map<String, Object> searchParameter);

        void handleFavoriteButtonClicked(ShopViewModel.ShopViewItem shopItem, int adapterPosition);
    }
}
