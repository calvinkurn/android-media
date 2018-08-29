package com.tokopedia.topads.dashboard.data.source;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsOldManagementApi;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/18/17.
 */

public class GetDepositTopAdsDataSourceCloud {

    private TopAdsOldManagementApi topAdsManagementApi;

    @Inject
    public GetDepositTopAdsDataSourceCloud(TopAdsOldManagementApi topAdsManagementApi) {
        this.topAdsManagementApi = topAdsManagementApi;
    }

    public Observable<DataDeposit> getDeposit(TKPDMapParam<String, String> params) {
        return topAdsManagementApi.getDashboardDeposit(params)
                .map(new SimpleDataResponseMapper<DataDeposit>());
    }
}
