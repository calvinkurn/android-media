package com.tokopedia.tkpdcontent.common.data.source.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author by milhamj on 06/02/18.
 */

public interface KolApi {
    @POST
    @Headers({"Content-Type: application/json"})
    Observable<Response<DataResponse<List<String>>>> getProfileKolData(@Body String requestBody);
}
