package com.tokopedia.core.network.apiservices.replacement.apis;

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
 * Created by hangnadi on 3/3/17.
 */
public interface ReplacementActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Replacement.PATH_CANCEL_REPLACEMENT)
    @Deprecated
    Observable<Response<TkpdResponse>> cancelReplacement(@FieldMap TKPDMapParam<String, Object> stringObjectTKPDMapParam);
}
