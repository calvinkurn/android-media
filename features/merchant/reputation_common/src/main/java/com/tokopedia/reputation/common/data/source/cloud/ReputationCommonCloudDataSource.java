package com.tokopedia.reputation.common.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.reputation.common.data.source.cloud.api.ReputationCommonApi;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeedList;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class ReputationCommonCloudDataSource {

    private ReputationCommonApi gmCommonApi;

    public ReputationCommonCloudDataSource(ReputationCommonApi reputationCommonApi) {
        this.gmCommonApi = reputationCommonApi;
    }

    public Observable<Response<DataResponse<ReputationSpeedList>>> getStatisticSpeed(String shopId) {
        return gmCommonApi.getStatisticSpeed(shopId);
    }
}
