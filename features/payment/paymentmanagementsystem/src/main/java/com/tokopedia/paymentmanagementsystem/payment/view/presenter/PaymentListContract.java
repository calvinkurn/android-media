package com.tokopedia.paymentmanagementsystem.payment.view.presenter;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.paymentmanagementsystem.payment.view.model.PaymentListModel;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public interface PaymentListContract {
    interface View extends BaseListViewListener<PaymentListModel>{

        void showCancelationMessage(String refundMessage, String transactionID, String merchantCode);

        void onResultCancelPayment(boolean isSuccess, String message);

        void onErrorGetCancelDetail(Throwable e);

        void onErrorCancelPayment(Throwable e);

        void showDialogLoading();

        void hideDialogLoading();
    }

    interface Presenter extends CustomerPresenter<View>{

        void getPaymentList(Resources resources, Context context, int page);

        void getCancelDetail(Resources resources, String transactionID, String merchantCode);

        void cancelPayment(Resources resources, String transactionID, String merchantCode);
    }
}
