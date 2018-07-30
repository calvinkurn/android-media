package com.tokopedia.imagepicker.picker.instagram.data.source.cloud;

import com.tokopedia.imagepicker.picker.instagram.data.model.ResponseGetAccessToken;
import com.tokopedia.imagepicker.picker.instagram.data.model.ResponseListMediaInstagram;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramConstant;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

public interface InstagramApi {

    @FormUrlEncoded
    @POST("/oauth/access_token")
    Observable<Response<ResponseGetAccessToken>> getAccessToken(@FieldMap Map<String, String> params);

    @GET(InstagramConstant.URL_PATH_GET_LIST_MEDIA)
    Observable<Response<ResponseListMediaInstagram>> getSelfMedia(
            @Header("Cookie") String cookies,
            @Query("access_token") String accessToken,
            @Query("max_id") String nextMaxId,
            @Query("count") String count
    );
}
