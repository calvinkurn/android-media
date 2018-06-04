package com.tokopedia.tkpdreactnative.react.singleauthpayment.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by zulfikarrahman on 4/5/18.
 */

public interface SinglePaymentConfirmationContract {
    interface Presenter extends CustomerPresenter<View> {
        void savePreferenceHide(boolean isShow);
        void getPreferenceHide();
    }

    interface View extends CustomerView {

        void hideProgressLoading();

        void showProgressLoading();

        void onSuccessSavePreference();

        void onErrorSavePreference();

        void onGetPreference(boolean isShow);

        void onErrorGetPreference();
    }
}
