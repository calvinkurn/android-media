package com.tokopedia.core.network.apiservices.user.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

import static com.tokopedia.core.network.apiservices.etc.apis.home.CategoryApi.HEADER_USER_ID;

/**
 * Created by nisie on 12/22/16.
 */

public interface OtpOnCallApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_OTP_WITH_CALL)
    Observable<Response<TkpdResponse>> requestOTPWithCall(@Header(HEADER_USER_ID) String userId,
                                                          @FieldMap Map<String, String> params);
}
