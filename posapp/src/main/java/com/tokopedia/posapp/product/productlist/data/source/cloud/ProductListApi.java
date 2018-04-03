package com.tokopedia.posapp.product.productlist.data.source.cloud;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.base.data.pojo.PosResponse;
import com.tokopedia.posapp.common.PosUrl;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductListData;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by okasurya on 10/13/17.
 */

public interface ProductListApi {

    @GET(PosUrl.Product.PRODUCT_LIST_V1)
    Observable<Response<TkpdResponse>> getProductList(@QueryMap Map<String, String> params);

    @GET(PosUrl.Product.PRODUCT_LIST_V2)
    Observable<Response<PosResponse<ProductListData>>> getProductListV2(@QueryMap Map<String, String> params);

    @GET(PosUrl.Product.GET_ETALASE)
    Observable<Response<TkpdResponse>> getEtalase(@QueryMap Map<String, String> params);

}
