package com.tokopedia.kyc;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface UpgradeToOvoContract {

    interface View extends CustomerView{
        void showSnackbarErrorMessage(String message);

        String getErrorMessage(Throwable e);

        void showProgressBar();

        void hideProgressBar();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getBalanceWallet();

        void onDestroyView();
    }

}
