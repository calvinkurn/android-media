package com.tokopedia.phoneverification.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.otp.cotp.view.viewmodel.RequestOtpViewModel;
import com.tokopedia.phoneverification.R;
import com.tokopedia.phoneverification.view.listener.PhoneVerification;

import rx.Subscriber;

/**
 * @author by nisie on 10/24/17.
 */

public class RequestOTPPhoneverificationSubscriber extends Subscriber<RequestOtpViewModel> {
    private final PhoneVerification.View viewListener;

    public RequestOTPPhoneverificationSubscriber(PhoneVerification.View viewListener) {
        this.viewListener = viewListener;
    }


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorRequestOTP(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(RequestOtpViewModel requestOtpViewModel) {
        if (requestOtpViewModel.isSuccess() && !TextUtils.isEmpty(requestOtpViewModel
                .getMessageStatus())) {
            viewListener.onSuccessRequestOtp(requestOtpViewModel.getMessageStatus());
        } else if (requestOtpViewModel.isSuccess()) {
            viewListener.onSuccessRequestOtp(MainApplication.getAppContext().getString(R.string
                    .success_request_otp));
        } else {
            viewListener.onErrorRequestOTP(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }
}
