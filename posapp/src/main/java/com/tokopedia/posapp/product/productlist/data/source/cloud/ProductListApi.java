package com.tokopedia.posapp.product.productlist.data.source.cloud;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by okasurya on 10/13/17.
 */

public interface ProductListApi {

    @GET(TkpdBaseURL.Pos.GET_PRODUCT_LIST)
    Observable<Response<TkpdResponse>> getProductList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Pos.GET_ETALASE)
    Observable<Response<TkpdResponse>> getEtalase(@QueryMap Map<String, String> params);

}
