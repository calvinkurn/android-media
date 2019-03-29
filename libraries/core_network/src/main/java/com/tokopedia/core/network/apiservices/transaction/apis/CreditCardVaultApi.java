package com.tokopedia.core.network.apiservices.transaction.apis;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kris on 8/21/17. Tokopedia
 */

@Deprecated
public interface CreditCardVaultApi {

    @POST(TkpdBaseURL.Payment.PATH_CC_DISPLAY)
    Observable<Response<String>> getListCreditCard(@Body JsonObject requestBody);

    @POST(TkpdBaseURL.Payment.PATH_CC_DELETE)
    Observable<Response<String>> deleteCreditCard(@Body JsonObject requestBody);

}