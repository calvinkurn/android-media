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

public interface FavoriteContract {
    interface View extends CustomerView {

        void addTopAdsShop(TopAdsShopViewModel shopViewModel);

        void addAllDataFavorite(List<Visitable> elementList, boolean clearData);

        void addFavoriteShop(FavoriteShopViewModel shopViewModel);

        void setRefreshing(boolean refreshing);

        boolean isLoading();

        void showLoading();

        void showErrorLoadMore();

        void showErrorLoadData();

    }

    interface Presenter extends CustomerPresenter<View> {
        void loadDataWishlistAndFavorite();

        void loadDataTopAdsShop();

        void setFavoriteShop(android.view.View view, TopAdsShopItem shopItem);

        void loadOnRefresh();

        void loadOnRetry();

        void loadOnMore();

        void onSaveDataBeforeRotate(Bundle outState);

        void onViewStateRestored(Bundle outState);
    }

}
