package com.tokopedia.loyalty.domain.apiservice;

import com.tokopedia.loyalty.domain.entity.response.TokoPointResponse;
import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public interface TokoPointApi {
    @GET(TkpdBaseURL.TokoPoint.VERSION +TkpdBaseURL.TokoPoint.GET_COUPON_LIST)
    Observable<Response<TokoPointResponse>> getCouponList(@QueryMap Map<String, String> params);

}
