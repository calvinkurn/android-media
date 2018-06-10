package com.tokopedia.otp.cotp.view.subscriber;

import com.tokopedia.otp.common.network.OtpErrorHandler;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.RequestOtpViewModel;

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
        view.onErrorGetOTP(OtpErrorHandler.getErrorMessage(e, view.getContext(), false));
    }

    @Override
    public void onNext(RequestOtpViewModel requestOtpViewModel) {
        view.dismissLoadingProgress();
        view.onSuccessGetOTP();
    }
}
