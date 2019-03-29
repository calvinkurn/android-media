package com.tokopedia.core.network.apiservices.drawer.api;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.drawer2.data.pojo.UserData;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

@Deprecated
public interface DrawerDataApi {

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<UserData>>> getConsumerDrawerData(@Body Map<String, Object> body);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<UserData>>> getSellerDrawerData(@Body Map<String, Object> body);
}
