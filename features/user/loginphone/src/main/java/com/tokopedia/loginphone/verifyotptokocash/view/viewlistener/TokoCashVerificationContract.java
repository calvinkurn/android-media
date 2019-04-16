package com.tokopedia.loginphone.verifyotptokocash.view.viewlistener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.loginphone.choosetokocashaccount.view.listener.ChooseTokocashAccountContract;
import com.tokopedia.loginphone.verifyotptokocash.domain.pojo.verifyotp.VerifyOtpTokoCashPojo;
/**
 * @author by nisie on 11/30/17.
 */

public interface TokoCashVerificationContract {
    interface View extends CustomerView, ChooseTokocashAccountContract.View {
        void onSuccessGetOTP();

        void onSuccessVerifyOTP(VerifyOtpTokoCashPojo verifyOtpTokoCashViewModel);

        void onErrorGetOTP(String errorMessage);

        void onErrorVerifyOtpCode(String errorMessage);

        void showLoadingProgress();

        void dismissLoadingProgress();

        void onErrorNoAccountTokoCash();

        boolean isCountdownFinished();

        void dropKeyboard();

        Context getContext();

        void onLimitOTPReached(String errorMessage);

        void autoLoginPhoneNumber(VerifyOtpTokoCashPojo userDetail);

        void onBackPressed();
    }

    interface Presenter extends CustomerPresenter<View> {

        void requestOTP(String phoneNumber, String type );

        void verifyOtp(String phoneNumber, String otpCode);

        void autoLogin(String key, VerifyOtpTokoCashPojo viewModel);
    }
}
