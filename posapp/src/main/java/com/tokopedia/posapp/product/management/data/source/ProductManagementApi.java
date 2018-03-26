package com.tokopedia.posapp.product.management.data.source;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.common.PosUrl;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author okasurya on 3/20/18.
 */

public interface ProductManagementApi {

    @GET(PosUrl.Product.PRODUCT_LIST_V2)
    Observable<Response<TkpdResponse>> getProducts(@QueryMap Map<String, String> params);
}
