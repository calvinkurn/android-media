package com.tokopedia.core.manage.people.bank.listener;

import android.content.Context;

import com.tokopedia.core.manage.people.bank.model.PaymentListModel;

/**
 * Created by kris on 7/24/17. Tokopedia
 */

public interface ListBankTypeActivityView {

    void onGetPaymentListData(PaymentListModel paymentListModel);

    Context getContext();

}
