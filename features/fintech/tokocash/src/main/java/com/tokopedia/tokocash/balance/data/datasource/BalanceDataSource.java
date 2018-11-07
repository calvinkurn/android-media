package com.tokopedia.tokocash.balance.data.datasource;

import com.tokopedia.tokocash.balance.data.entity.BalanceTokoCashEntity;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public interface BalanceDataSource {

    Observable<BalanceTokoCashEntity> getBalanceTokoCash();

}