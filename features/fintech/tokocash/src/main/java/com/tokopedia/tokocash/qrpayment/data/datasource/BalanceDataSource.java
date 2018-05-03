package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.core.drawer2.data.pojo.Wallet;
import com.tokopedia.tokocash.qrpayment.data.entity.BalanceTokoCashEntity;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public interface BalanceDataSource {

    Observable<Wallet> getBalanceTokoCash();

}