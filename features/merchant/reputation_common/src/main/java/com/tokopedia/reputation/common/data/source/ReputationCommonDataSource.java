package com.tokopedia.reputation.common.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.reputation.common.data.source.cloud.ReputationCommonCloudDataSource;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeedList;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class ReputationCommonDataSource {
    private ReputationCommonCloudDataSource reputationCommonCloudDataSource;

    public ReputationCommonDataSource(ReputationCommonCloudDataSource reputationCommonCloudDataSource) {
        this.reputationCommonCloudDataSource = reputationCommonCloudDataSource;
    }

    public Observable<ReputationSpeed> getStatisticSpeed(String shopId) {
        return reputationCommonCloudDataSource.getStatisticSpeed(shopId).flatMap(new Func1<Response<DataResponse<ReputationSpeedList>>, Observable<ReputationSpeed>>() {
            @Override
            public Observable<ReputationSpeed> call(Response<DataResponse<ReputationSpeedList>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData().getSpeed());
            }
        });
    }
}
