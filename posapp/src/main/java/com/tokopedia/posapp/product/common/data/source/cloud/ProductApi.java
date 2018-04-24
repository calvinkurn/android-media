package com.tokopedia.posapp.product.common.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.base.data.pojo.PosResponse;
import com.tokopedia.posapp.base.data.pojo.PosSimpleResponse;
import com.tokopedia.posapp.common.PosUrl;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.management.data.pojo.ProductListData;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductDetail;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductList;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by okasurya on 10/13/17.
 */

public interface ProductApi {

    @GET(PosUrl.Product.GET_CUSTOMER_PRODUCT_LIST)
    Observable<Response<DataResponse<ProductList>>> getProductDetail(@Path(ProductConstant.Key.OUTLET_ID) String outletId,
                                                                     @QueryMap Map<String, String> params);

    @GET(PosUrl.Product.GET_CUSTOMER_PRODUCT_LIST)
    Observable<Response<PosSimpleResponse<List<ProductDetail>>>> getProductList(@Path(ProductConstant.Key.OUTLET_ID) String outletId,
                                                                   @QueryMap Map<String, String> params);

    @GET(PosUrl.Product.GET_ETALASE)
    Observable<Response<TkpdResponse>> getEtalase(@QueryMap Map<String, String> params);

}
