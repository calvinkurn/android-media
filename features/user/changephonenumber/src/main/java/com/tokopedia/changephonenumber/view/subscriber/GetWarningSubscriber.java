package com.tokopedia.changephonenumber.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.changephonenumber.view.viewmodel.WarningViewModel;

import rx.Subscriber;

/**
 * Created by milhamj on 27/12/17.
 */

public class GetWarningSubscriber extends Subscriber<WarningViewModel> {
    private final ChangePhoneNumberWarningFragmentListener.View view;

    public GetWarningSubscriber(ChangePhoneNumberWarningFragmentListener.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onGetWarningError(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(WarningViewModel object) {
        if (object.isOvoEligible() && !TextUtils.isEmpty(object.getUrlOvo())) {
            view.goToOvoWebView(object.getUrlOvo());
        } else {
            view.onGetWarningSuccess(object);
        }
    }
}
