package com.tokopedia.loginregister.activation.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.sessioncommon.data.model.TokenViewModel;

/**
 * @author by nisie on 10/19/18.
 */
public interface ActivationContract {

    interface View extends CustomerView {

        void showLoadingProgress();

        void finishLoadingProgress();

        void onErrorResendActivation(String errorMessage);

        Context getContext();

        void onSuccessResendActivation();

        void onErrorActivateWithUnicode(String errorMessage);

        void onSuccessActivateWithUnicode(TokenViewModel activateUnicodePojo);
    }

    interface Presenter extends CustomerPresenter<View> {
        void activateAccount(String email, String unicode);

        void resendActivation(String email);
    }
}
