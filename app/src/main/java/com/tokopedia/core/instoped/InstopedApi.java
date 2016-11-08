package com.tokopedia.core.instoped;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Tkpd_Eka on 4/6/2016.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public interface InstopedApi {

    @FormUrlEncoded
    @POST("/oauth/access_token")
    Observable<Response<String>> getAccessToken(@FieldMap Map<String, String> params);

    @GET("/v1/users/self/media/recent")
    Observable<Response<String>> getSelfMedia(
            @Query("access_token") String accessToken,
            @Query("max_id") String nextMaxId,
            @Query("count") String count
    );

}
