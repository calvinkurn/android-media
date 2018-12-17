package com.tokopedia.home.account.presentation;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;
import com.tokopedia.home.account.presentation.listener.BaseAccountView;



/**
 * @author okasurya on 7/20/18.
 */
public interface SellerAccount {

    interface View extends BaseAccountView {
        void loadSellerData(SellerViewModel sellerViewModel);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getSellerData(String query, String topadsQuery);
    }
}
