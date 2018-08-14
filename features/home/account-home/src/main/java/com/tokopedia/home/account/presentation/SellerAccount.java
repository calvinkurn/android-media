package com.tokopedia.home.account.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;

/**
 * @author okasurya on 7/20/18.
 */
public interface SellerAccount {
    interface View extends CustomerView {
        void loadSellerData(SellerViewModel model);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getSellerData(String query);
    }
}
