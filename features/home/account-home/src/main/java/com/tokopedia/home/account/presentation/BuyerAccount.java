package com.tokopedia.home.account.presentation;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.account.presentation.listener.BaseAccountView;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;


/**
 * @author okasurya on 7/17/18.
 */
public interface BuyerAccount {
    interface View extends BaseAccountView {
        void loadBuyerData(BuyerViewModel model);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getBuyerData(String query, String saldoQuery);
    }
}
