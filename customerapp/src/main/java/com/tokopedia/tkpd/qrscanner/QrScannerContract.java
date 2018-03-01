package com.tokopedia.tkpd.qrscanner;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by sandeepgoyal on 18/12/17.
 */

public interface QrScannerContract {

    interface View extends com.tokopedia.abstraction.base.view.listener.CustomerView {
        void finish();

        void startActivity(Intent intent);

        void startActivityForResult(Intent intent, int requestCode);

        void showProgressDialog();

        void hideProgressDialog();

        void showErrorGetInfo(String message);

        void showErrorNetwork(String message);

        int getRequestCodeForQrPayment();

        void interruptToLoginPage();

        Activity getActivity();
    }

    interface Presenter extends CustomerPresenter<View> {
        void onBarCodeScanComplete(String barcodeData);

        void onScanCompleteAfterLoginQrPayment();

        void destroyView();
    }
}
