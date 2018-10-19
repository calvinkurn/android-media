package com.tokopedia.phoneverification.view.subscriber;

import com.tokopedia.phoneverification.data.model.ValidateVerifyPhoneNumberDomain;
import com.tokopedia.phoneverification.view.listener.PhoneVerification;

import rx.Subscriber;

/**
 * @author by nisie on 10/24/17.
 */

public class VerifyPhoneNumberSubscriber extends Subscriber<ValidateVerifyPhoneNumberDomain> {
    private final PhoneVerification.View viewListener;

    public VerifyPhoneNumberSubscriber(PhoneVerification.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorVerifyPhoneNumber(e.getLocalizedMessage());
    }

    @Override
    public void onNext(ValidateVerifyPhoneNumberDomain validateVerifyPhoneNumberDomain) {
        if (validateVerifyPhoneNumberDomain.getValidateOtpDomain().isSuccess()
                && validateVerifyPhoneNumberDomain.getVerifyPhoneDomain().isSuccess()) {
            viewListener.onSuccessVerifyPhoneNumber();
        } else {
            viewListener.onErrorVerifyPhoneNumber(validateVerifyPhoneNumberDomain.getVerifyPhoneDomain().getStatusMessage());
        }
    }
}
