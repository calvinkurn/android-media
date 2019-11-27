package com.tokopedia.tkpd.qrscanner;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.ovo.model.BarcodeResponseData;

/**
 * Created by sandeepgoyal on 18/12/17.
 */

public interface QrScannerContract {

    interface View extends com.tokopedia.abstraction.base.view.listener.CustomerView {
        void finish();

        void startActivity(Intent intent);

        void showProgressDialog();

        void hideProgressDialog();

        void showErrorGetInfo(String message);

        void showErrorNetwork(Throwable throwable);

        Activity getActivity();

        void goToPaymentPage(String imeiNumber, BarcodeResponseData barcodeData);

        boolean getRemoteConfigForOvoPay();
    }

    interface Presenter extends CustomerPresenter<View> {
        void onBarCodeScanComplete(String barcodeData);

        void onScanCompleteAfterLoginQrPayment();

        void destroyView();
    }
}
