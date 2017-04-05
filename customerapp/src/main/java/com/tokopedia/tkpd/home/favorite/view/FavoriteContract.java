package com.tokopedia.tkpd.home.favorite.view;


import android.os.Bundle;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.FavoriteShopViewModel;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopItem;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopViewModel;

import java.util.List;

/**
 * @author Kulomady on 1/19/17.
 */

interface FavoriteContract {

    interface View extends CustomerView {

        void addTopAdsShop(TopAdsShopViewModel shopViewModel);

        void showDataFavorite(List<Visitable> elementList);

        void showMoreDataFavoriteShop(List<Visitable> elementList);

        void addFavoriteShop(FavoriteShopViewModel shopViewModel);

        void showRefreshLoading();

        void hideRefreshLoading();

        boolean isLoading();

        void showLoadMoreLoading();

        void showErrorLoadMore();

        void showErrorLoadData();

        void hasToShowWishlistFailedMessage();

        void hasToDismissWishlistFailedMessage();

        void validateMessageError();

        void showTopAdsProductError();
    }

    interface Presenter extends CustomerPresenter<View> {

        void loadDataWishlistAndFavorite();

        void loadDataTopAdsShop();

        void addFavoriteShop(android.view.View view, TopAdsShopItem shopItem);

        void refreshAllDataFavoritePage();

        void loadMoreFavoriteShop();

        void onSaveDataBeforeRotate(Bundle outState);

        void onViewStateRestored(Bundle outState);
    }

}
