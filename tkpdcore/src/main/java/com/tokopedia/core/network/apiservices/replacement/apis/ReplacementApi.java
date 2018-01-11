package com.tokopedia.core.network.apiservices.replacement.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by nisie on 3/3/17.
 */
@Deprecated
public interface ReplacementApi {

    @GET(TkpdBaseURL.Replacement.PATH_GET_OPPORTUNITY)
    Observable<Response<TkpdResponse>> getOpportunityList(@QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.Replacement.PATH_GET_CATEGORY)
    Observable<Response<TkpdResponse>> getOpportunityCategory(@QueryMap TKPDMapParam<String, Object> param);
}
