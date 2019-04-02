package com.tokopedia.tokocash.activation.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by nabillasabbaha on 2/1/18.
 */

public interface LinkedTokoCashContract {

    interface View extends CustomerView {
        void onSuccessLinkWalletToTokoCash();

        void onErrorLinkWalletToTokoCash(Throwable e);

        void onErrorNetwork(Throwable e);

        void showProgressDialog();
    }

    interface Presenter extends CustomerPresenter<View> {
        void linkWalletToTokoCash(String otp);

        String getUserPhoneNumber();

        boolean isMsisdnUserVerified();

        void destroyView();
    }
}
