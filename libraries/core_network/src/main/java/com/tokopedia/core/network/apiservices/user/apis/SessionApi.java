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
public interface SessionApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_LOGIN)
    Observable<Response<TkpdResponse>> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_CREATE_PASSWORD)
    Observable<Response<TkpdResponse>> createPassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_DO_LOGIN)
    Observable<Response<TkpdResponse>> doLogin(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_DO_LOGIN_PLUS)
    Observable<Response<TkpdResponse>> doLoginPlus(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_DO_LOGIN_PLUS)
    Observable<Response<String>> doLoginPlusx(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_LOGOUT)
    Observable<Response<TkpdResponse>> logout(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_BYPASS_LOGIN)
    Observable<Response<TkpdResponse>> loginBypass(@FieldMap Map<String, String> params);
}
