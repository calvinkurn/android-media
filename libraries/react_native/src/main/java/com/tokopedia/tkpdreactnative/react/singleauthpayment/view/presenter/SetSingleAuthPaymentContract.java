package com.tokopedia.tkpdreactnative.react.singleauthpayment.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by kris on 4/24/18. Tokopedia
 */

public interface SetSingleAuthPaymentContract {
    interface Presenter extends CustomerPresenter<View> {

        void setSingleAuthenticationMode();

    }

    interface View extends CustomerView {

        void hideProgressLoading();

        void showProgressLoading();

        void onErrorNetworkSingleAuth(String errorMessage);

        void onSuccessSingleAuth(String successMessage);
    }
}
