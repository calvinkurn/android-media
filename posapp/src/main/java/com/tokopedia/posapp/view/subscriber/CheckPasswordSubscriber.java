package com.tokopedia.posapp.view.subscriber;

import com.tokopedia.posapp.domain.model.CheckPasswordDomain;
import com.tokopedia.posapp.view.DialogPassword;

import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by okasurya on 9/27/17.
 */

public class CheckPasswordSubscriber extends Subscriber<CheckPasswordDomain> {
    DialogPassword.View view;

    public CheckPasswordSubscriber(DialogPassword.View view) {
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
    public void onNext(CheckPasswordDomain result) {
        if(result.isStatus()) {
            view.onCheckPasswordSuccess();
        } else {
            view.onCheckPasswordError("Password yang anda masukkan salah");
        }
    }
}
