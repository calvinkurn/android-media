package com.tokopedia.core.network.apiservices.transaction.apis;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.constants.TkpdBaseURL;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kris on 10/13/17. Tokopedia
 */

@Deprecated
public interface CreditCardAuthApi {

    @POST(TkpdBaseURL.Payment.PATH_ZEUS_CHECK_WHITELIST)
    Observable<Response<String>> checkWhiteList(@Body JsonObject requestBody);

    @POST(TkpdBaseURL.Payment.PATH_ZEUS_UPDATE_WHITELIST)
    Observable<Response<String>> updateWhiteList(@Body JsonObject requestBody);

}
