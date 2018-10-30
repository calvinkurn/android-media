package com.tokopedia.loginphone.verifyotptokocash.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.loginphone.verifyotptokocash.domain.pojo.requestotp.RequestOtpTokoCashPojo;
import com.tokopedia.loginphone.verifyotptokocash.view.viewlistener.TokoCashVerificationContract;
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
        view.onErrorGetOTP(ErrorHandlerSession.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(RequestOtpTokoCashPojo requestOtpTokoCashViewModel) {
        view.dismissLoadingProgress();
        view.onSuccessGetOTP();

    }
}
