package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.digital.tokocash.model.WalletToken;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashData;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 9/4/17.
 */

public interface ITokoCashBalanceInteractor {

    void getBalanceTokoCash(Subscriber<TokoCashData> subscriber);

    void getTokenWallet(Subscriber<WalletToken> subscriber);
}
