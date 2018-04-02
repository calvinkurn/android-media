package com.tokopedia.gamification.data;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity;
import com.tokopedia.gamification.data.entity.ResponseTokenTokopointEntity;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public interface GamificationApi {

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<GraphqlResponse<ResponseTokenTokopointEntity>> getTokenTokopoints(@Body Map<String, String> requestBodyMap);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<GraphqlResponse<ResponseCrackResultEntity>> getCrackResult(@Body String requestBody);
}
