package com.tokopedia.core.manage.people.bank.presenter;

import com.tokopedia.core.manage.people.bank.model.BcaOneClickData;

import rx.Subscriber;

/**
 * Created by kris on 7/24/17. Tokopedia
 */

public interface ListBankTypePresenter {

    void onOneClickBcaChosen(Subscriber<BcaOneClickData> subscriber);

    void onDestroyed();
}
