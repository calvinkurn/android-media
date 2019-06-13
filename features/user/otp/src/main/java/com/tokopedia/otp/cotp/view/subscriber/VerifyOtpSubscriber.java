package com.tokopedia.otp.cotp.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.otp.common.network.OtpErrorCode;
import com.tokopedia.otp.common.network.OtpErrorHandler;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.ValidateOtpDomain;
import com.tokopedia.otp.R;

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
        String errorMessage = OtpErrorHandler.getErrorMessage(e, view.getContext(), true);
        view.onErrorVerifyOtpCode(errorMessage);

        if (!TextUtils.isEmpty(e.getMessage())
                && errorMessage.contains(view.getContext().getString(R.string
                .default_request_error_unknown))) {
            view.logUnknownError(e);
        }
    }

    @Override
    public void onNext(ValidateOtpDomain validateOtpDomain) {
        view.dismissLoadingProgress();
        if (validateOtpDomain.isSuccess())
            view.onSuccessVerifyOTP(validateOtpDomain.getUuid(), validateOtpDomain.getMsisdn());
        else {
            view.onErrorVerifyOtpCode(OtpErrorHandler.getDefaultErrorCodeMessage(OtpErrorCode
                    .UNSUPPORTED_FLOW, view.getContext()));
            view.logUnknownError(new Throwable("Validate is not success"));
        }
    }
}
