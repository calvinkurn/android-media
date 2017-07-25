package com.tokopedia.core.network.apiservices.payment.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kris on 7/24/17. Tokopedia
 */

public interface BcaOneClickApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Payment.PATH_POST_ONE_CLICK)
    Observable<Response<String>>
    getBcaOneClickAccessToken(@FieldMap TKPDMapParam<String, String> params);
}
