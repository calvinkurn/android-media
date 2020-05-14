package com.tokopedia.digital.newcart.presentation.compoundview.listener;

/**
 * @author anggaprasetiyo on 3/20/17.
 */

public interface IOtpVerificationView extends IBaseView {

    void renderSuccessReRequestSmsOtp(String message);

    void renderErrorTimeoutReRequestSmsOtp(String messageErrorTimeout);

    void renderErrorNoConnectionReRequestSmsOtp(String messageErrorNoConnection);

    void renderErrorReRequestSmsOtp(String message);

    void renderErrorResponseReRequestSmsOtp(String message);


    void renderSuccessFirstRequestSmsOtp(String message);

    void renderErrorTimeoutFirstRequestSmsOtp(String messageErrorTimeout);

    void renderErrorNoConnectionFirstRequestSmsOtp(String messageErrorNoConnection);

    void renderErrorFirstRequestSmsOtp(String message);

    void renderErrorResponseFirstRequestSmsOtp(String message);


    void renderSuccessRequestCallOtp(String message);

    void renderErrorTimeoutRequestCallOtp(String messageErrorTimeout);

    void renderErrorNoConnectionRequestCallOtp(String messageErrorNoConnection);

    void renderErrorRequestCallOtp(String message);

    void renderErrorResponseRequestCallOtp(String message);


    void renderSuccessVerifyOtp(String message);

    void renderErrorTimeoutVerifyOtp(String messageErrorTimeout);

    void renderErrorNoConnectionVerifyOtp(String messageErrorNoConnection);

    void renderErrorVerifyOtp(String message);

    void renderErrorResponseVerifyOtp(String message);


    String getUserId();

    String getOtpCode();


}
