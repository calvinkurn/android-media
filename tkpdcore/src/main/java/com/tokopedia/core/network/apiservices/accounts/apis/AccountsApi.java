package com.tokopedia.core.network.apiservices.accounts.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

import static com.tokopedia.core.network.apiservices.etc.apis.home.CategoryApi.HEADER_USER_ID;

/**
 * AccountsApi
 * Created by stevenfredian on 5/25/16.
 */
public interface AccountsApi {

    @GET(TkpdBaseURL.Accounts.PATH_DISCOVER_LOGIN)
    Observable<Response<TkpdResponse>> discoverLogin(@QueryMap TKPDMapParam<String, Object> parameters);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.GENERATE_HOST)
    Observable<GeneratedHost> generateHost(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.OTP.REQUEST_OTP)
    Observable<Response<TkpdResponse>> requestOtp(@Header(HEADER_USER_ID) String userId,
                                                  @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.OTP.VALIDATE_OTP)
    Observable<Response<TkpdResponse>> validateOtp(@FieldMap TKPDMapParam<String, Object> param);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.OTP.REQUEST_OTP_EMAIL)
    Observable<Response<TkpdResponse>> requestOtpToEmail(@FieldMap TKPDMapParam<String, Object> parameters);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_MAKE_LOGIN)
    Call<String> makeLoginsynchronous(@FieldMap TKPDMapParam<String, Object> parameters);

    @GET(TkpdBaseURL.Accounts.Wallet.GET_BALANCE)
    Observable<Response<TkpdResponse>> getTokoCash(@QueryMap TKPDMapParam<String, Object> params);

}
