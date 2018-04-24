package com.tokopedia.topads.common.domain.repository;

import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by nakama on 23/04/18.
 */

public interface TopAdsShopDepositRepository {

    Observable<DataDeposit> getDeposit(RequestParams requestParams);
}
