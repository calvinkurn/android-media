package com.tokopedia.otp.cotp.view.subscriber;

import com.tokopedia.otp.common.network.OtpErrorCode;
import com.tokopedia.otp.common.network.OtpErrorHandler;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.ValidateOtpDomain;

import rx.Subscriber;

/**
 * @author by nisie on 12/4/17.
 */

public class VerifyOtpSubscriber extends Subscriber<ValidateOtpDomain> {
    private final Verification.View view;

    public VerifyOtpSubscriber(Verification.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoadingProgress();
        view.onErrorVerifyOtpCode(OtpErrorHandler.getErrorMessage(e, view.getContext()));
    }

    @Override
    public void onNext(ValidateOtpDomain validateOtpDomain) {
        view.dismissLoadingProgress();
        if (validateOtpDomain.isSuccess())
            view.onSuccessVerifyOTP();
        else {
            view.onErrorVerifyOtpCode(OtpErrorHandler.getDefaultErrorCodeMessage(OtpErrorCode
                    .UNSUPPORTED_FLOW, view.getContext()));
        }
    }
}
