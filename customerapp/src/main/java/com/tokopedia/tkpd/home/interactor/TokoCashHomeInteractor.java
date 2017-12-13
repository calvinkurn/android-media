package com.tokopedia.tkpd.home.interactor;

import com.tokopedia.digital.tokocash.model.CashBackData;

import rx.Subscriber;

/**
 * Created by kris on 6/15/17. Tokopedia
 */

public interface TokoCashHomeInteractor {

    void requestPendingCashBack(Subscriber<CashBackData> subscriber);

    void onDestroy();
}
