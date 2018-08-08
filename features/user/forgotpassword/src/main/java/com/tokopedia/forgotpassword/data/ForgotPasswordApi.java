package com.tokopedia.forgotpassword.data;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by nisie on 8/8/18.
 */
public interface ForgotPasswordApi {

    @FormUrlEncoded
    @POST(ForgotPasswordUrl.RESET_PASSWORD)
    Observable<Response<TkpdResponse>> resetPassword(@FieldMap Map<String, String> params);
}
