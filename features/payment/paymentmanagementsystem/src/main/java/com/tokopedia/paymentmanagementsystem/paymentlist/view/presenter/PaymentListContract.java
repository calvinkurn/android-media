package com.tokopedia.paymentmanagementsystem.paymentlist.view.presenter;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.model.PaymentListModel;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public interface PaymentListContract {
    interface View extends BaseListViewListener<PaymentListModel>{

        void showCancelationMessage(String refundMessage);

        void onResultCancelPayment(boolean success);

        void onErrorGetCancelDetail(Throwable e);

        void onErrorCancelPayment(Throwable e);
    }

    interface Presenter extends CustomerPresenter<View>{

        void getPaymentList(Resources resources);

        void getCancelDetail(Resources resources, String transactionID, String merchantCode);

        void cancelPayment(Resources resources, String transactionID, String merchantCode);
    }
}
