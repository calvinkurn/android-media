package com.tokopedia.tkpd.qrscanner;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

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

        void interruptToLoginPage();

        Activity getActivity();

        Observable<InfoQrTokoCash> getInfoQrTokoCash(RequestParams requestParams);

        Observable<BalanceTokoCash> getBalanceTokoCash();

        void navigateToNominalActivityPage(String qrcode, InfoQrTokoCash infoQrTokoCash);
    }

    interface Presenter extends CustomerPresenter<View> {
        void onBarCodeScanComplete(String barcodeData);

        void onScanCompleteAfterLoginQrPayment();

        void destroyView();
    }
}
