package com.tokopedia.tokocash.balance.domain;

import com.tokopedia.tokocash.balance.view.BalanceTokoCash;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public interface IBalanceRepository {

    Observable<BalanceTokoCash> getBalanceTokoCash();

    Observable<BalanceTokoCash> getLocalBalanceTokoCash();

}
