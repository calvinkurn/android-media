package com.tokopedia.forgotpassword.data;

import com.tokopedia.forgotpassword.data.pojo.ResetPasswordPojo;
import com.tokopedia.network.data.model.response.DataResponse;

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
    Observable<Response<DataResponse<ResetPasswordPojo>>> resetPassword(@FieldMap Map<String,
            Object> params);
}
