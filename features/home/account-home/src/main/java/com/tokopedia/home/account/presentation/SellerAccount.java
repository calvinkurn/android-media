package com.tokopedia.home.account.presentation;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.account.presentation.listener.BaseAccountView;
import com.tokopedia.home.account.presentation.viewmodel.base.AccountViewModel;

import java.util.List;

/**
 * @author okasurya on 7/20/18.
 */
public interface SellerAccount {

    interface View extends BaseAccountView {
        void loadData(AccountViewModel accountViewModel);
    }

    interface Presenter extends CustomerPresenter<BuyerAccount.View> {
        void getData();
    }
}
