package com.tokopedia.gm.common.data.source.cloud;

import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi;
import com.tokopedia.gm.common.data.source.cloud.model.RequestCashbackModel;
import com.tokopedia.network.data.model.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class GMCommonCloudDataSource {

    private GMCommonApi gmCommonApi;

    @Inject
    public GMCommonCloudDataSource(GMCommonApi gmCommonApi) {
        this.gmCommonApi = gmCommonApi;
    }

    public Observable<Response<DataResponse<String>>> setCashback(RequestCashbackModel requestCashbackModel) {
        return gmCommonApi.setCashback(requestCashbackModel);
    }
}
