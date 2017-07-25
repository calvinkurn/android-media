package com.tokopedia.core.drawer2.domain;

import com.tokopedia.core.drawer2.data.pojo.deposit.DepositModel;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by nisie on 5/4/17.
 */

public interface DepositRepository {

    Observable<DepositModel> getDeposit(TKPDMapParam<String, Object> params);
}
