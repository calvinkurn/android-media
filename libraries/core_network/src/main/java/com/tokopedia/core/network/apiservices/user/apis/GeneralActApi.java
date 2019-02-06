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

@Deprecated
public interface GeneralActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_ACTIVATE_CODE)
    Observable<Response<TkpdResponse>> activateCode(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_CANCEL_EDIT_EMAIL)
    Observable<Response<TkpdResponse>> cancelEditEmail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_CONFIRM_EEDIT_EMAIL)
    Observable<Response<TkpdResponse>> confirmEditEmail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_EMAIL)
    Observable<Response<TkpdResponse>> editEmail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_RESEND_CODE)
    Observable<Response<TkpdResponse>> resendCode(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_RESET_PASSWORD)
    Observable<Response<TkpdResponse>> resetPassword(@FieldMap Map<String, String> params);
}
