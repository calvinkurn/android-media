package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.search.result.presentation.model.ShopViewModel;
import com.tokopedia.search.result.presentation.view.listener.FavoriteActionListener;
import com.tokopedia.search.result.presentation.view.listener.RequestDynamicFilterListener;
import com.tokopedia.search.result.presentation.view.listener.SearchShopListener;

import java.util.Map;

public interface ShopListSectionContract {

    interface View extends SearchSectionContract.View {
        void logDebug(String tag, String message);

        void launchLoginActivity(String shopId);

        boolean isUserHasLogin();

        String getUserId();

        void disableFavoriteButton(int adapterPosition);

        void enableFavoriteButton(int adapterPosition);

        String getQueryKey();

        void backToTop();

        BaseAppComponent getBaseAppComponent();

        Map<String, Object> getSearchParameter();
    }

    interface Presenter extends SearchSectionContract.Presenter<View> {
        void setFavoriteActionListener(FavoriteActionListener favoriteActionListener);

        void setRequestDynamicFilterListener(RequestDynamicFilterListener requestDynamicFilterListener);

        void setSearchShopListener(SearchShopListener searchShopListener);

        void loadShop(Map<String, Object> searchParameter);

        void handleFavoriteButtonClicked(ShopViewModel.ShopViewItem shopItem, int adapterPosition);
    }
}
