package com.tokopedia.home.common;

import retrofit2.Response;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by henrypriyono on 26/01/18.
 */

public interface HomeAceApi {
    @POST("personalization/v1/ulabel/userLocation")
    @Headers({"Content-Type: application/json"})
    Observable<Response<String>> sendGeolocationInfo();
}