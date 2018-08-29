package com.tokopedia.posapp.auth;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by okasurya on 9/28/17.
 */

public interface AccountApi {
    @POST(TkpdBaseURL.User.PATH_VALIDATE_PASSWORD)
    @FormUrlEncoded
    Observable<Response<TkpdResponse>> validatePassword(@FieldMap Map<String, String> params);
}
