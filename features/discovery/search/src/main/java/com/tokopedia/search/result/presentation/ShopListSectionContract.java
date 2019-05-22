package com.tokopedia.search.result.presentation;

import com.tokopedia.search.result.presentation.model.ShopViewModel;
import com.tokopedia.search.result.presentation.view.listener.FavoriteActionListener;
import com.tokopedia.search.result.presentation.view.listener.RequestDynamicFilterListener;
import com.tokopedia.search.result.presentation.view.listener.SearchShopListener;

import java.util.Map;

public interface ShopListSectionContract {

    interface View extends SearchSectionContract.View {
        boolean isUserHasLogin();

        String getUserId();

        void disableFavoriteButton(int adapterPosition);

        void enableFavoriteButton(int adapterPosition);
    }

    interface Presenter extends SearchSectionContract.Presenter<View> {
        void setFavoriteActionListener(FavoriteActionListener favoriteActionListener);

        void setSearchShopListener(SearchShopListener searchShopListener);

        void loadShop(Map<String, Object> searchParameter);

        void handleFavoriteButtonClicked(ShopViewModel.ShopViewItem shopItem, int adapterPosition);
    }
}
