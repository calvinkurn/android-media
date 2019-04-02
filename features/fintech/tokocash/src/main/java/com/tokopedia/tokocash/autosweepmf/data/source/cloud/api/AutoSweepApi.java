package com.tokopedia.tokocash.autosweepmf.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepLimit;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Retrofit interface for autosweepmf apis
 */
public interface AutoSweepApi {
    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<ResponseAutoSweepDetail>>> getAutoSweepDetail(@Body String body);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<ResponseAutoSweepLimit>>> postAutoSweepLimit(@Body Map<String, Object> body);
}
