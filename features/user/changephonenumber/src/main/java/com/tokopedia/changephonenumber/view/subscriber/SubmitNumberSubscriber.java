package com.tokopedia.changephonenumber.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.changephonenumber.R;
import com.tokopedia.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;

import rx.Subscriber;

/**
 * Created by milhamj on 03/01/18.
 */

public class SubmitNumberSubscriber extends Subscriber<Boolean> {
    private final ChangePhoneNumberInputFragmentListener.View view;

    public SubmitNumberSubscriber(ChangePhoneNumberInputFragmentListener.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoading();
        view.onSubmitNumberError(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(Boolean isSuccess) {
        view.dismissLoading();
        if (isSuccess)
            view.onSubmitNumberSuccess();
        else
            view.onSubmitNumberError(view.getContext().getString(R.string.default_request_error_unknown));
    }
}
