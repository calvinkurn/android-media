package com.tokopedia.otp.cotp.view.viewlistener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.otp.cotp.view.viewmodel.ListVerificationMethod;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;

/**
 * @author by nisie on 11/30/17.
 */

public interface SelectVerification {

    interface View extends CustomerView {
        void onMethodSelected(MethodItem methodItem);

        void showLoading();

        void dismissLoading();

        void onSuccessGetList(ListVerificationMethod listVerificationMethod);

        void onErrorGetList(String errorCodeMessage);

        Context getContext();

        void logUnknownError(Throwable message);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getMethodList(String phoneNumber, int otpType);
    }
}
