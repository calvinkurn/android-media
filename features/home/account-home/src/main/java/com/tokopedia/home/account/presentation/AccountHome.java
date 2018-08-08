package com.tokopedia.home.account.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.account.presentation.listener.BaseAccountView;
import com.tokopedia.home.account.presentation.viewmodel.base.AccountViewModel;

/**
 * @author okasurya on 7/20/18.
 */
public interface AccountHome {

    interface Presenter extends CustomerPresenter<View> {
        void getAccount(String query);
    }

    interface View extends BaseAccountView {
        void renderData(AccountViewModel accountViewModel);
    }
}
