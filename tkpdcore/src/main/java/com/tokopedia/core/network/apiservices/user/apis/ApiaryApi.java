package com.tokopedia.core.network.apiservices.user.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by nisie on 12/22/16.
 */

public interface ApiaryApi {

    @GET(TkpdBaseURL.User.PATH_OTP_WITH_CALL)
    Observable<Response<TkpdResponse>> requestOTPWithCall(@QueryMap Map<String, String> params);
}
