package com.tokopedia.posapp.outlet.data.source;

import com.tokopedia.posapp.base.data.pojo.PosResponse;
import com.tokopedia.posapp.common.PosUrl;
import com.tokopedia.posapp.outlet.data.pojo.OutletResponse;

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
    Observable<Response<PosResponse<OutletResponse>>> getOutlet(@QueryMap Map<String, String> params);
}
