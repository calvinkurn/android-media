package com.tokopedia.posapp.auth.validatepassword.view;

import com.tokopedia.posapp.auth.validatepassword.domain.model.ValidatePasswordDomain;
import com.tokopedia.posapp.auth.validatepassword.view.ValidatePassword;

import rx.Subscriber;

/**
 * Created by okasurya on 9/27/17.
 */

public class CheckPasswordSubscriber extends Subscriber<ValidatePasswordDomain> {
    ValidatePassword.View view;

    public CheckPasswordSubscriber(ValidatePassword.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.onCheckPasswordError(e);
    }

    @Override
    public void onNext(ValidatePasswordDomain result) {
        if(result.isStatus()) {
            view.onCheckPasswordSuccess();
        } else {
            view.onCheckPasswordError("Password yang anda masukkan salah");
        }
    }
}
