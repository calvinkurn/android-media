package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public interface IBalanceRepository {

    Observable<BalanceTokoCash> getBalanceTokoCash();

    Observable<BalanceTokoCash> getLocalBalanceTokoCash();

}
