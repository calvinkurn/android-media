package com.tokopedia.search.result.presentation;

import com.tokopedia.search.result.presentation.model.ShopViewModel;

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

        void launchLoginActivity(String shopId);

        void onErrorToggleFavorite(Throwable throwable, int adapterPosition);

        void onErrorToggleFavorite(int adapterPosition);

        void onSuccessToggleFavorite(int adapterPosition, boolean targetFavoritedStatus);
    }

    interface Presenter extends SearchSectionContract.Presenter<View> {
        void loadShop(Map<String, Object> searchParameter);

        void handleFavoriteButtonClicked(ShopViewModel.ShopViewItem shopItem, int adapterPosition);
    }
}
