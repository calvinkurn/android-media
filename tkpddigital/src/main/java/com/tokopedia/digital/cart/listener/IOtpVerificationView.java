package com.tokopedia.digital.cart.listener;

/**
 * @author anggaprasetiyo on 3/20/17.
 */

public interface IOtpVerificationView extends IBaseView {

    void renderSuccessRequestOtp(String message);

    void renderErrorRequestOtp(String message);

    String getUserId();

    String getOtpCode();
}
