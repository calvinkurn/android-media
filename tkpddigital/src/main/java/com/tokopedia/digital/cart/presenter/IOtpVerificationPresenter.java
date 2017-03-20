package com.tokopedia.digital.cart.presenter;

/**
 * @author anggaprasetiyo on 3/20/17.
 */

public interface IOtpVerificationPresenter {
    String TAG = IOtpVerificationPresenter.class.getSimpleName();

    void processRequestOtp();

    void processVerifyOtp();
}
