package com.tokopedia.tkpd.msisdn.presenter;

/**
 * Created by Nisie on 7/13/16.
 */
public interface MsisdnVerificationFragmentPresenter {

    void setReminder(boolean isChecked);

    void verifyOTP(String otpCode);

    void requestOTP(String phoneNumber);

    void onDestroyView();
}
