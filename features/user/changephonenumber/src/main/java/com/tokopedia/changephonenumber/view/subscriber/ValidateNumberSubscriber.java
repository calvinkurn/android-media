package com.tokopedia.changephonenumber.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.changephonenumber.R;
import com.tokopedia.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;

import rx.Subscriber;

/**
 * Created by milhamj on 03/01/18.
 */

public class ValidateNumberSubscriber extends Subscriber<Boolean> {
    private final ChangePhoneNumberInputFragmentListener.View view;
    int UNSUPPORTED_FLOW = 1005;


    public ValidateNumberSubscriber(ChangePhoneNumberInputFragmentListener.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoading();
        view.onValidateNumberError(ErrorHandler.getErrorMessage(view.getContext(),
                e));
    }

    @Override
    public void onNext(Boolean isSuccess) {
        view.dismissLoading();
        if (isSuccess)
            view.onValidateNumberSuccess();
        else
            view.onValidateNumberError(view.getContext().getString(R.string.default_request_error_unknown));
    }
}
