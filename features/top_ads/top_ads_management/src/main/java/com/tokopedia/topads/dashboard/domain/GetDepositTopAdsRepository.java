package com.tokopedia.topads.dashboard.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/18/17.
 */

public interface GetDepositTopAdsRepository {
    Observable<DataDeposit> getDeposit(TKPDMapParam<String, String> params);
}
