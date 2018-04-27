package com.tokopedia.otp.cotp.view.viewlistener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;

/**
 * @author by nisie on 11/30/17.
 */

public interface Verification {
    interface View extends CustomerView {
        void onSuccessGetOTP();

        void onSuccessVerifyOTP();

        void onErrorGetOTP(String errorMessage);

        void onErrorVerifyOtpCode(String errorMessage);

        void showLoadingProgress();

        void dismissLoadingProgress();

        boolean isCountdownFinished();

        void dropKeyboard();

        void onGoToPhoneVerification();

        void onErrorVerifyLogin(String errorMessage);

        void onErrorVerifyOtpCode(int resId);

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<Verification.View> {

        void requestOTP(VerificationViewModel viewModel, VerificationPassModel passModel);

        void verifyOtp(VerificationPassModel phoneNumber, String otpCode);
    }
}
