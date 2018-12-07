package com.tokopedia.otp.cotp.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.otp.common.network.OtpErrorHandler;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.RequestOtpViewModel;
import com.tokopedia.otp.R;

import rx.Subscriber;

/**
 * @author by nisie on 12/4/17.
 */

public class RequestOtpSubscriber extends Subscriber<RequestOtpViewModel> {

    private final Verification.View view;

    public RequestOtpSubscriber(Verification.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {


    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoadingProgress();
        String errorMessage = OtpErrorHandler.getErrorMessage(e, view.getContext(), true);
        if (errorMessage.contains(view.getContext().getString(R.string
                .limit_otp_reached_many_times))) {
            view.onLimitOTPReached(OtpErrorHandler.getErrorMessage(e, view.getContext(), false));
        } else {
            view.onErrorGetOTP(errorMessage);

            if (!TextUtils.isEmpty(e.getMessage())
                    && errorMessage.contains(view.getContext().getString(R.string.default_request_error_unknown))) {
                view.logUnknownError(e);
            }
        }
    }

    @Override
    public void onNext(RequestOtpViewModel requestOtpViewModel) {
        view.dismissLoadingProgress();
        view.onSuccessGetOTP(requestOtpViewModel.getMessageStatus());
    }
}
