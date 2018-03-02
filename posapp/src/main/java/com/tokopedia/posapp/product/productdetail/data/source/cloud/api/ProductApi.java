package com.tokopedia.posapp.product.productdetail.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by okasurya on 8/10/17.
 */

public interface ProductApi {
    @GET(TkpdBaseURL.Product.V4_PRODUCT + TkpdBaseURL.Product.PATH_GET_DETAIL_PRODUCT)
    Observable<Response<TkpdResponse>> getProductDetail(@QueryMap Map<String, String> params);
}
