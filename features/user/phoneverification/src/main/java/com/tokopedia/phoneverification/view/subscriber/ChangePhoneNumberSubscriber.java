package com.tokopedia.phoneverification.view.subscriber;


import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.phoneverification.data.model.ChangePhoneNumberViewModel;
import com.tokopedia.phoneverification.view.listener.ChangePhoneNumber;

import rx.Subscriber;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangePhoneNumberSubscriber extends Subscriber<ChangePhoneNumberViewModel> {

    private final ChangePhoneNumber.View viewListener;

    public ChangePhoneNumberSubscriber(ChangePhoneNumber.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorChangePhoneNumber(ErrorHandler.getErrorMessage(viewListener.getContext(), e));
    }

    @Override
    public void onNext(ChangePhoneNumberViewModel changePhoneNumberViewModel) {
        if (changePhoneNumberViewModel.isSuccess())
            viewListener.onSuccessChangePhoneNumber();
        else
            viewListener.onErrorChangePhoneNumber(changePhoneNumberViewModel.getStatusMessage());
    }
}
