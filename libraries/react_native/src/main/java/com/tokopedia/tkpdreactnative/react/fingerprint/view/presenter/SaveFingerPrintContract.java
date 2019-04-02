package com.tokopedia.tkpdreactnative.react.fingerprint.view.presenter;

import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by zulfikarrahman on 4/5/18.
 */

public interface SaveFingerPrintContract {
    interface Presenter extends CustomerPresenter<View> {
        void registerFingerPrint(String transactionId, String publicKey, String date, String accountSignature, String userId);
    }

    interface View extends CustomerView {

        void hideProgressLoading();

        void showProgressLoading();

        void onErrorNetworkRegisterFingerPrint(Throwable e);

        void onSuccessRegisterFingerPrint();
    }
}
