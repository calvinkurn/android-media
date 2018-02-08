package com.tokopedia.posapp.data.source.cloud.api;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public interface AceApi {
    @GET("v1/web-service/shop/get_shop_product")
    Observable<Response<TkpdResponse>> getProductList(@QueryMap Map<String, String> params);
}
