package com.tokopedia.core.network.apiservices.user.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface MSISDNActApi {
    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_DO_VERIFICATION_MSISDN)
    Observable<Response<TkpdResponse>> doVerification(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_SEND_EMAIL_CHANGE_PHONE_NUMBER)
    Observable<Response<TkpdResponse>> sendEmailChangePhone(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_SEND_VERIFICATION_OTP)
    Observable<Response<TkpdResponse>> sendVerificationOTP(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_SKIP_UPDATE)
    Observable<Response<TkpdResponse>> skipUpdate(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_VALIDATE_EMAIL_CODE)
    Observable<Response<TkpdResponse>> validateEmailCode(@FieldMap Map<String, String> params);
}
