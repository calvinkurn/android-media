package com.tokopedia.phoneverification.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.user.session.UserSession;

/**
 * Created by nisie on 2/23/17.
 */
public interface PhoneVerification {
    interface View extends CustomerView {
        void onSuccessVerifyPhoneNumber();

        void onErrorVerifyPhoneNumber(String errorMessage);

        UserSession getUserSession();
    }

    interface Presenter extends CustomerPresenter<View> {
        void verifyPhoneNumber(String phoneNumber);
    }
}
