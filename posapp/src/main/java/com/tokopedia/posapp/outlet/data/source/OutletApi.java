package com.tokopedia.posapp.outlet.data.source;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.common.PosUrl;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by okasurya on 8/4/17.
 */

public interface OutletApi {
    @GET(PosUrl.Shop.OUTLET_LIST_V1)
    Observable<Response<TkpdResponse>> getAddress(@QueryMap Map<String, String> params);
}
