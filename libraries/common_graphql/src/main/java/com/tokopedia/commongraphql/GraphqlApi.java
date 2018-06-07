package com.tokopedia.commongraphql;

import java.util.HashMap;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface GraphqlApi {

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<String> request(@Body HashMap<String, String> requestBodyMap);
}
