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

    @Deprecated
    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.PATH_GET_TOKEN)
    Observable<Response<String>> getTokenOld(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.PATH_GET_TOKEN)
    Observable<Response<String>> getToken(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.PATH_GET_TOKEN)
    Call<String> getTokenSynchronous(@FieldMap Map<String, String> params);

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
    Observable<Response<TkpdResponse>> resentActivation(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.GENERATE_HOST)
    Observable<GeneratedHost> generateHost(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Truecaller.VERIFY_PHONE)
    Observable<Response<TkpdResponse>> verifyPhone(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.OTP.REQUEST_OTP)
    Observable<Response<TkpdResponse>> requestOtp(@Header(HEADER_USER_ID) String userId,
                                                  @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.OTP.VALIDATE_OTP)
    Observable<Response<TkpdResponse>> validateOtp(@FieldMap TKPDMapParam<String, Object> param);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.MSISDN.VERIFY_PHONE_NUMBER)
    Observable<Response<TkpdResponse>> verifyPhoneNumber(@FieldMap TKPDMapParam<String, Object> param);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.Image.GET_UPLOAD_HOST)
    Observable<Response<TkpdResponse>> getUploadHost(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.Image.VALIDATE_SIZE)
    Observable<Response<TkpdResponse>> validateImage(@FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.Image.SUBMIT_DETAIL)
    Observable<Response<TkpdResponse>> submitImage(@FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.Ktp.CHECK_STATUS)
    Observable<Response<TkpdResponse>> checkStatusKtp(@FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.OTP.REQUEST_OTP_EMAIL)
    Observable<Response<TkpdResponse>> requestOtpToEmail(@FieldMap TKPDMapParam<String, Object> parameters);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.OTP.REQUEST_OTP_EMAIL)
    Observable<Response<TkpdResponse>> requestOtpToEmail(@Header(HEADER_USER_ID) String userId,
                                                         @FieldMap TKPDMapParam<String, Object> parameters);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.DO_REGISTER)
    Observable<Response<TkpdResponse>> registerEmail(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.ACTIVATE_UNICODE)
    Observable<Response<String>> activateWithUnicode(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.CHANGE_EMAIL)
    Observable<Response<TkpdResponse>> changeEmail(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.MSISDN.CHANGE_PHONE_NUMBER)
    Observable<Response<TkpdResponse>> changePhoneNumber(@FieldMap TKPDMapParam<String, Object> parameters);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_MAKE_LOGIN)
    Observable<Response<TkpdResponse>> makeLogin(@FieldMap TKPDMapParam<String, Object> parameters);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_MAKE_LOGIN)
    Call<String> makeLoginsynchronous(@FieldMap TKPDMapParam<String, Object> parameters);

    @GET(TkpdBaseURL.Accounts.PATH_GET_INFO)
    Observable<Response<String>> getUserInfo(@QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.PATH_EDIT_PROFILE)
    Observable<Response<TkpdResponse>> editProfile(@FieldMap TKPDMapParam<String, Object> parameters);

    @GET(TkpdBaseURL.Accounts.ChangeMSISDN.GET_WARNING)
    Observable<Response<TkpdResponse>> getWarning(@QueryMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.ChangeMSISDN.SEND_EMAIL)
    Observable<Response<TkpdResponse>> sendEmail(@FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.ChangeMSISDN.VALIDATE_EMAIL_CODE)
    Observable<Response<TkpdResponse>> validateEmailCode(@FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.ChangeMSISDN.VALIDATE)
    Observable<Response<TkpdResponse>> validateNumber(@FieldMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.Accounts.PATH_DISCOVER_REGISTER)
    Observable<Response<TkpdResponse>> discoverRegister();

}
