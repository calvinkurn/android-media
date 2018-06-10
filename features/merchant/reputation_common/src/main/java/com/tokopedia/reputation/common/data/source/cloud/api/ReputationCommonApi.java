package com.tokopedia.reputation.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.reputation.common.constant.ReputationCommonUrl;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeedList;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface ReputationCommonApi {

    @GET(ReputationCommonUrl.STATISTIC_SPEED_URL)
    Observable<Response<DataResponse<ReputationSpeedList>>> getStatisticSpeed(@Path("shop_id") String shopId);

}
