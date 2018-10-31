package com.tokopedia.loginphone.verifyotptokocash.view.subscriber;

import com.tokopedia.loginphone.R;
import com.tokopedia.loginphone.common.network.TokoCashErrorException;
import com.tokopedia.loginphone.verifyotptokocash.domain.pojo.requestotp.RequestOtpTokoCashPojo;
import com.tokopedia.loginphone.verifyotptokocash.view.viewlistener.TokoCashVerificationContract;
import com.tokopedia.otp.common.network.OtpErrorHandler;
import com.tokopedia.sessioncommon.ErrorHandlerSession;

import rx.Subscriber;

/**
 * @author by nisie on 12/4/17.
 */

public class RequestOtpTokoCashSubscriber extends Subscriber<RequestOtpTokoCashPojo> {

    private final TokoCashVerificationContract.View view;

    public RequestOtpTokoCashSubscriber(TokoCashVerificationContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {


    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoadingProgress();
        String errorMessage = getErrorMessage(e);
        if (errorMessage.contains(view.getContext().getString(R.string
                .limit_otp_reached_many_times))) {
            view.onLimitOTPReached(OtpErrorHandler.getErrorMessage(e, view.getContext(), false));
        } else {
            view.onErrorGetOTP(errorMessage);
        }
    }

    private String getErrorMessage(Throwable e) {
        if (e instanceof TokoCashErrorException) {
            return ((TokoCashErrorException) e).getErrorMessage();
        } else {
            return OtpErrorHandler.getErrorMessage(e, view.getContext(), true);
        }
    }

    @Override
    public void onNext(RequestOtpTokoCashPojo requestOtpTokoCashViewModel) {
        view.dismissLoadingProgress();
        view.onSuccessGetOTP();

    }
}
