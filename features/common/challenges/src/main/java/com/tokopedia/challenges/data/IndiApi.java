package com.tokopedia.challenges.data;

import com.tokopedia.nps.data.source.ChallengesUrl;

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

    @Headers("x-api-key: EERIxwXF644c1E1To5puL8xNP5PvLHSv240PyNYf")
    @GET(ChallengesUrl.Auth.ACCESS_TOKEN)
    Call<String> getAccessToken();

    @Headers("x-api-key: EERIxwXF644c1E1To5puL8xNP5PvLHSv240PyNYf")
    @FormUrlEncoded
    @POST(ChallengesUrl.MANAGE.USER_MAP)
    Call<String> mapUser(@FieldMap Map<String, String> params, @Header("authorization") String token);
}
