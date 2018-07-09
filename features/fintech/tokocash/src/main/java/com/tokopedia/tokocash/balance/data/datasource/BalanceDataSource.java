package com.tokopedia.tokocash.balance.data.datasource;

import com.tokopedia.core.drawer2.data.pojo.Wallet;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public interface BalanceDataSource {

    Observable<Wallet> getBalanceTokoCash();

}