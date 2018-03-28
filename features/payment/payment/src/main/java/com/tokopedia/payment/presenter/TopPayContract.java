package com.tokopedia.payment.presenter;

import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.payment.model.PaymentPassData;

/**
 * Created by zulfikarrahman on 3/27/18.
 */

public interface TopPayContract {
    interface Presenter extends CustomerPresenter<View> {
        String CHARSET_UTF_8 = "UTF-8";

        void proccessUriPayment();

        void registerFingerPrint(String transactionId, String publicKey, String date, String accountSignature, String userId);

        void paymentFingerPrint(String transactionId, String partner, String publicKey, String date, String accountSignature, String userId);

        String getUserId();
    }

    interface View extends CustomerView {

        void hideProgressLoading();

        void showProgressLoading();

        void renderWebViewPostUrl(String url, byte[] postData);

        void showToastMessageWithForceCloseView(String message);

        void showToastMessage(String message);

        void callbackPaymentCanceled();

        void callbackPaymentFailed();

        void callbackPaymentSucceed();

        void hideProgressBar();

        void showProgressBar();

        void showTimeoutErrorOnUiThread();

        void setWebPageTitle(String title);

        void backStackAction();

        String getStringFromResource(int resId);

        PaymentPassData getPaymentPassData();

        void navigateToActivity(Intent intentCart);

        void onSuccessRegisterFingerPrint();

        void hideProgressBarDialog();

        void onErrorRegisterFingerPrint(Throwable e);

        void showProgressDialog();

        void onErrorPaymentFingerPrint(Throwable e);

        void onSuccessPaymentFingerprint(String url, String paramEncode);
    }
}
