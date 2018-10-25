package com.tokopedia.phoneverification.view.subscriber;

import com.tokopedia.phoneverification.data.VerifyPhoneNumberDomain;
import com.tokopedia.phoneverification.view.listener.PhoneVerification;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by alvinatin on 25/10/18.
 */

public class VerifyPhoneNumberSubscriber extends Subscriber<VerifyPhoneNumberDomain> {

    private final PhoneVerification.View view;

    @Inject
    public VerifyPhoneNumberSubscriber(PhoneVerification.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        view.onErrorVerifyPhoneNumber(throwable.getLocalizedMessage());
    }

    @Override
    public void onNext(VerifyPhoneNumberDomain verifyPhoneNumberDomain) {
        if (verifyPhoneNumberDomain.isSuccess()) {
            view.onSuccessVerifyPhoneNumber();
        } else {
            view.onErrorVerifyPhoneNumber("");
        }
    }
}
