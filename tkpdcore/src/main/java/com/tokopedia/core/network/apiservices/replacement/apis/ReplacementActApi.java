package com.tokopedia.core.network.apiservices.replacement.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by hangnadi on 3/3/17.
 */

public interface ReplacementActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Replacement.PATH_ACCEPT_REPLACEMENT)
    Observable<Response<TkpdResponse>> acceptReplacement(@FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Replacement.PATH_CANCEL_REPLACEMENT)
    Observable<Response<TkpdResponse>> cancelReplacement(@FieldMap TKPDMapParam<String, Object> stringObjectTKPDMapParam);

}
