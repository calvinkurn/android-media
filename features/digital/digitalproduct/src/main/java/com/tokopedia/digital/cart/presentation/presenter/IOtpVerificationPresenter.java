package com.tokopedia.digital.cart.presentation.presenter;

/**
 * @author anggaprasetiyo on 3/20/17.
 */

public interface IOtpVerificationPresenter {
    String TAG = IOtpVerificationPresenter.class.getSimpleName();

    void processFirstRequestSmsOtp();

    void processReRequestSmsOtp();

    void processRequestCallOtp();

    void processVerifyOtp();
}
