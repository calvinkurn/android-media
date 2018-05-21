package com.tokopedia.topads.common.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.topads.common.data.api.TopAdsManagementApi;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.usecase.RequestParams;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hadi.putra on 23/04/18.
 */

public class ShopDepositDataSourceCloud {

    private final TopAdsManagementApi topAdsManagementApi;

    public ShopDepositDataSourceCloud(TopAdsManagementApi topAdsManagementApi) {
        this.topAdsManagementApi = topAdsManagementApi;
    }

    public Observable<DataDeposit> getDeposit(RequestParams requestParams){
        return topAdsManagementApi.getDashboardDeposit(requestParams.getParamsAllValueInString())
                .map(new Func1<Response<DataResponse<DataDeposit>>, DataDeposit>() {
                    @Override
                    public DataDeposit call(Response<DataResponse<DataDeposit>> dataResponse) {
                        return dataResponse.body().getData();
                    }
                });
    }
}
