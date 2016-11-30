package com.tokopedia.core.msisdn.listener;

import android.app.Activity;
import android.widget.EditText;

/**
 * Created by Nisie on 7/13/16.
 */
public interface MsisdnVerificationFragmentView {
    Activity getActivity();

    void showError(String errorMessage);

    String getPhoneNumber();

    String getVerificationCode();

    void setError(EditText view, String error);

    void removeError();

    void showLoading();

    void finishLoading();

    void onDismiss();

    EditText getOtpEditText();

    EditText getPhoneEditText();

    void onSuccessRequestOTP();

    void onSuccessVerifyOTP();

    void setSuccessRequestOTPCache();
}
