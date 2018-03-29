package com.tokopedia.gamification.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity;
import com.tokopedia.gamification.data.entity.ResponseTokenTokopointEntity;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public interface GamificationApi {

    @POST("graphql")
    @Headers({"Content-Type: application/json"})
    Observable<DataResponse<ResponseTokenTokopointEntity>> getTokenTokopoints(@Body String requestBody);

    @POST("graphql")
    @Headers({"Content-Type: application/json"})
    Observable<DataResponse<ResponseCrackResultEntity>> getCrackResult(@Body String requestBody);
}
