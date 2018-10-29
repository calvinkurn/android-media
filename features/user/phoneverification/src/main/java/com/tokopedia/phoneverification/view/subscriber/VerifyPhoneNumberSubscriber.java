package com.tokopedia.phoneverification.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.phoneverification.data.VerifyPhoneNumberDomain;
import com.tokopedia.phoneverification.view.listener.PhoneVerification;

import rx.Subscriber;

/**
 * @author by alvinatin on 25/10/18.
 */

public class VerifyPhoneNumberSubscriber extends Subscriber<VerifyPhoneNumberDomain> {

    private final PhoneVerification.View view;

    public VerifyPhoneNumberSubscriber(PhoneVerification.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        view.onErrorVerifyPhoneNumber(ErrorHandler.getErrorMessage(view.getContext(), throwable));

    }

    @Override
    public void onNext(VerifyPhoneNumberDomain verifyPhoneNumberDomain) {
        if (verifyPhoneNumberDomain.isSuccess()) {
            view.onSuccessVerifyPhoneNumber();
        } else {
            view.onErrorVerifyPhoneNumber(verifyPhoneNumberDomain.getStatusMessage());
        }
    }
}
