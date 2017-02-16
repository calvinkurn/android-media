package com.tokopedia.core.network.apiservices.accounts.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * AccountsApi
 * Created by stevenfredian on 5/25/16.
 */
public interface AccountsApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.PATH_GET_TOKEN)
    Observable<Response<String>> getToken(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Accounts.PATH_GET_INFO)
    Observable<Response<String>> getInfo(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Accounts.PATH_GET_PROFILE)
    Observable<Response<String>> getProfile(@Path("id") String id);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_MAKE_LOGIN)
    Observable<Response<TkpdResponse>> makeLogin(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Accounts.PATH_DISCOVER_LOGIN)
    Observable<Response<TkpdResponse>> discoverLogin();

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.DO_REGISTER)
    Observable<Response<TkpdResponse>> doRegister(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.CREATE_PASSWORD)
    Observable<Response<TkpdResponse>> createPassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.RESET_PASSWORD)
    Observable<Response<TkpdResponse>> resetPassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.VALIDATE_EMAIL)
    Observable<Response<TkpdResponse>> validateEmail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.RESENT_ACTIVATION)
    Observable<Response<TkpdResponse>> resentActivation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.GENERATE_HOST)
    Observable<GeneratedHost> generateHost(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(TkpdBaseURL.Truecaller.VERIFY_PHONE)
    Observable<Response<TkpdResponse>> verifyPhone(@FieldMap Map<String, String> params);

}
