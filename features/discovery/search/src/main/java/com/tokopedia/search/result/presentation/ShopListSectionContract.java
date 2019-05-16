package com.tokopedia.search.result.presentation;

import android.os.Bundle;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.listener.FavoriteActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;
import com.tokopedia.search.result.presentation.view.listener.RequestDynamicFilterListener;
import com.tokopedia.search.result.presentation.view.listener.ShopListLoadMoreListener;

import java.util.Map;

public interface ShopListSectionContract {

    interface View extends SearchSectionContract.View {
        void logDebug(String tag, String message);

        void launchLoginActivity(String productId);

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

        void loadShop(Map<String, Object> searchParameter, ShopListLoadMoreListener loadMoreListener);

        void handleFavoriteButtonClicked(ShopViewModel.ShopItem shopItem, int adapterPosition);
    }
}
