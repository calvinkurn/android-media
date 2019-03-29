package com.tokopedia.changephonenumber.view.subscriber;

import com.tokopedia.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;

import rx.Subscriber;

/**
 * @author by alvinatin on 11/05/18.
 */

public class ValidateOtpStatusSubscriber extends Subscriber<Boolean> {

    private final ChangePhoneNumberWarningFragmentListener.View view;

    public ValidateOtpStatusSubscriber(ChangePhoneNumberWarningFragmentListener.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        view.onGetValidateOtpStatusError();
    }

    @Override
    public void onNext(Boolean isValid) {
        view.onGetValidateOtpStatusSuccess(isValid);
    }
}
