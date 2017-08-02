package com.tokopedia.core.manage.people.bank.presenter;

import com.tokopedia.core.manage.people.bank.model.BcaOneClickData;
import com.tokopedia.core.manage.people.bank.model.PaymentListModel;

import rx.Subscriber;

/**
 * Created by kris on 7/24/17. Tokopedia
 */

public interface ListBankTypePresenter {

    void onOneClickBcaChosen(Subscriber<BcaOneClickData> subscriber);

    void onGetPaymentList(Subscriber<PaymentListModel> subscriber);

    void onDeletePaymentList(Subscriber<PaymentListModel> subscriber, String tokenId);

    void onDestroyed();
}
