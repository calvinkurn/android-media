package com.tokopedia.posapp.shop.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.posapp.shop.view.viewmodel.ShopViewModel;

/**
 * Created by okasurya on 8/3/17.
 */

public interface Shop {
    interface View extends CustomerView {
        void onSuccessGetShop(ShopViewModel shop);

        void onErrorGetShop(String errorMessage);

        void startLoading();

        void finishLoading();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getUserShop();
    }
}
