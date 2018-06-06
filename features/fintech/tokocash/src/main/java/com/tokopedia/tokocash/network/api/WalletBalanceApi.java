package com.tokopedia.tokocash.network.api;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.drawer2.data.pojo.UserData;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface WalletBalanceApi {

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<UserData>>> getBalance(@Body String requestBody);
}
