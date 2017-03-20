package com.tokopedia.tkpd.home.favorite.view;


import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * @author Kulomady on 1/19/17.
 */

public interface FavoriteContract {
    interface View extends CustomerView {

        void loadTopAdsShop();

        void loadWishlistAndFavoriteShop();
    }

    interface Presenter extends CustomerPresenter<View> {
        void loadDataWishlistAndFavorite();

        void loadDataTopAdsShop();
    }

}
