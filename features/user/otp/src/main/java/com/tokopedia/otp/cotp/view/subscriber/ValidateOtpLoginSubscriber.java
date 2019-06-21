package com.tokopedia.otp.cotp.view.subscriber;

import com.tokopedia.otp.R;
import com.tokopedia.otp.common.network.OtpErrorCode;
import com.tokopedia.otp.common.network.OtpErrorHandler;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.ValidateOtpLoginDomain;

import rx.Subscriber;

/**
 * @author by nisie on 12/28/17.
 */

public class ValidateOtpLoginSubscriber extends Subscriber<ValidateOtpLoginDomain> {
    private final Verification.View view;

    public ValidateOtpLoginSubscriber(Verification.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoadingProgress();
        view.onErrorVerifyOtpCode(OtpErrorHandler.getErrorMessage(e, view.getContext(), false));
    }

    @Override
    public void onNext(ValidateOtpLoginDomain validateOTPLoginDomain) {
        view.dismissLoadingProgress();
        if (!validateOTPLoginDomain.getValidateOtpDomain().isSuccess()) {
            view.onErrorVerifyOtpCode(R.string.default_request_error_unknown);
        } else if (validateOTPLoginDomain.getValidateOtpDomain().isSuccess()
                && validateOTPLoginDomain.getMakeLoginDomain().isLogin()) {
            if (!validateOTPLoginDomain.getMakeLoginDomain().isMsisdnVerified()) {
                view.onGoToPhoneVerification();
            } else {
                view.onSuccessVerifyOTP(validateOTPLoginDomain.getValidateOtpDomain().getUuid(),
                        validateOTPLoginDomain.getValidateOtpDomain().getMsisdn());
            }
        } else {
            view.onErrorVerifyLogin(OtpErrorHandler.getDefaultErrorCodeMessage(OtpErrorCode
                    .UNSUPPORTED_FLOW, view.getContext()));
        }
    }
}
