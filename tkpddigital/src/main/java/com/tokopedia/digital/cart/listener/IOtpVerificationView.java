package com.tokopedia.digital.cart.listener;

/**
 * @author anggaprasetiyo on 3/20/17.
 */

public interface IOtpVerificationView extends IBaseView {

    void renderSuccessRequestOtp(String message);

    void renderErrorTimeoutRequestOtp(String messageErrorTimeout);

    void renderErrorNoConnectionRequestOtp(String messageErrorNoConnection);

    void renderErrorRequestOtp(String message);


    void renderSuccessVerifyOtp(String message);

    void renderErrorTimeoutVerifyOtp(String messageErrorTimeout);

    void renderErrorNoConnectionVerifyOtp(String messageErrorNoConnection);

    void renderErrorVerifyOtp(String message);



    String getUserId();

    String getOtpCode();


}
