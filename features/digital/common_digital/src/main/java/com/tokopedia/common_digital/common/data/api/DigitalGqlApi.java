package com.tokopedia.common_digital.common.data.api;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.common_digital.product.data.response.RechargeResponseEntity;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Rizky on 14/08/18.
 */
public interface DigitalGqlApi {

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<List<GraphqlResponse<RechargeResponseEntity>>>> getCategory(@Body String requestBody);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<List<GraphqlResponse<RechargeResponseEntity>>>> getCategoryAndFavoriteList(@Body String requestBody);

}