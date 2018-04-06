package com.tokopedia.core.network.apiservices.drawer.api;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.drawer2.data.pojo.SellerDrawerData;
import com.tokopedia.core.drawer2.data.pojo.UserDrawerData;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface DrawerDataApi {

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<UserDrawerData>>> getConsumerDrawerData(@Body String requestBody);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<SellerDrawerData>>> getSellerDrawerData(@Body String requestBody);
}
