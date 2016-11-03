package com.tokopedia.tkpd.network.apiservices.clover.apis;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * CloverApi
 * Created by Nisie on 4/6/16.
 */
public interface CloverApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_GET_TOPPOINTS_CLOVER)
    Observable<Response<TkpdResponse>> getTopPoints(@FieldMap Map<String, String> params);

}
