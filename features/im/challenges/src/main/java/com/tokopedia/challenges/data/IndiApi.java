package com.tokopedia.challenges.data;

import com.tokopedia.challenges.data.source.ChallengesUrl;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Vishal Gupta on 8/7/18.
 */
public interface IndiApi {

    @GET(ChallengesUrl.Auth.ACCESS_TOKEN)
    Call<String> getAccessToken(@Header("x-api-key") String apiKey);

    @FormUrlEncoded
    @POST(ChallengesUrl.MANAGE.USER_MAP)
    Call<String> mapUser(@FieldMap Map<String, String> params, @Header("authorization") String token, @Header("x-api-key") String apiKey);
}
