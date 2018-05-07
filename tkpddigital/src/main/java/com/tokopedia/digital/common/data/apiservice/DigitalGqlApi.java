package com.tokopedia.digital.common.data.apiservice;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.drawer2.data.pojo.UserData;
import com.tokopedia.digital.common.data.entity.response.RechargeCategoryDetailEntity;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface DigitalGqlApi {

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<RechargeCategoryDetailEntity>>> getCategory(@Body String requestBody);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<UserData>>> getFavoriteList(@Body String requestBody);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<UserData>>> getCategoryAndFavoriteList(@Body String requestBody);
}
