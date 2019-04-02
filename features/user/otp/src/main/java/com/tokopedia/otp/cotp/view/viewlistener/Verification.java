package com.tokopedia.otp.cotp.view.viewlistener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;

/**
 * @author by nisie on 11/30/17.
 */

public interface Verification {
    interface View extends CustomerView {
        void onSuccessGetOTP(String message);

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

        void trackOnBackPressed();

        void onLimitOTPReached(String errorMessage);

        void logUnknownError(Throwable e);

        void onSuccessGetModelFromServer(MethodItem methodItem);
    }

    interface Presenter extends CustomerPresenter<Verification.View> {

        void requestOTP(VerificationViewModel viewModel);

        void verifyOtp(int otpType, String phoneNumber, String email, String otpCode);
    }
}
