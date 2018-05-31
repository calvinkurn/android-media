package com.tokopedia.posapp.product.management.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.posapp.base.data.pojo.PosSimpleResponse;
import com.tokopedia.posapp.common.PosUrl;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductDetail;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author okasurya on 3/20/18.
 */

public interface ProductManagementApi {
    @GET(PosUrl.Product.GET_ADMIN_PRODUCT_LIST)
    Observable<Response<PosSimpleResponse<List<ProductDetail>>>> getProducts(@QueryMap Map<String, String> params);

    @POST(PosUrl.Product.EDIT_PRODUCT)
    @Headers({PosUrl.ContentType.JSON})
    Observable<Response<PosSimpleResponse<String>>> editProduct(@Path(ProductConstant.Key.OUTLET_ID) String outletId,
                                                                @Body JsonObject requestBody);
}
