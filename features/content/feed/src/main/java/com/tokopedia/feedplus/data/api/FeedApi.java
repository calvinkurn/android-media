package com.tokopedia.feedplus.data.api;

import com.tokopedia.abstraction.common.data.model.request.GraphqlRequest;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.feedplus.data.pojo.FeedQuery;
import com.tokopedia.kol.feature.post.data.pojo.FollowKolQuery;
import com.tokopedia.kolcommon.data.pojo.WhitelistQuery;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by milhamj on 08/06/18.
 */

public interface FeedApi {
    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<FeedQuery>>>
    getFeedData(@Body GraphqlRequest requestBody);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<FollowKolQuery>>>
    followKol(@Body GraphqlRequest requestBody);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<WhitelistQuery>>>
    getWhiteList(@Body GraphqlRequest requestBody);
}
