package com.tokopedia.favorite.view;


import android.os.Bundle;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.favorite.view.viewmodel.FavoriteShopViewModel;
import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem;

import java.util.List;

/**
 * @author Kulomady on 1/19/17.
 */

interface FavoriteContract {

    interface View extends CustomerView {

        void refreshDataFavorite(List<Visitable> elementList);

        void showInitialDataPage(List<Visitable> dataFavorite);

        void showMoreDataFavoriteShop(List<Visitable> elementList);

        void addFavoriteShop(FavoriteShopViewModel shopViewModel);

        void showRefreshLoading();

        void hideRefreshLoading();

        void stopTracePerformanceMonitoring();

        boolean isLoading();

        void showLoadMoreLoading();

        void showErrorLoadMore();

        void showErrorLoadData();

        void showFavoriteShopFailedMessage();

        void dismissFavoriteShopFailedMessage();

        void showTopadsShopFailedMessage();

        void dismissTopadsShopFailedMessage();

        void validateMessageError();

        void showErrorAddFavoriteShop();

        void stopLoadingFavoriteShop();

        void sendFavoriteShopImpression(String clickUrl);
    }

    interface Presenter extends CustomerPresenter<View> {

        void loadInitialData();

        void addFavoriteShop(android.view.View view, TopAdsShopItem shopItem);

        void refreshAllDataFavoritePage();

        void loadMoreFavoriteShop();

        void onSaveDataBeforeRotate(Bundle outState);

        void onViewStateRestored(Bundle outState);
    }

}
