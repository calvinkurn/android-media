package com.tokopedia.topads.dashboard.domain;

import com.tokopedia.topads.dashboard.data.model.DataDeposit;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/18/17.
 */

public interface GetDepositTopAdsRepository {
    Observable<DataDeposit> getDeposit(HashMap<String, String> params);
}
