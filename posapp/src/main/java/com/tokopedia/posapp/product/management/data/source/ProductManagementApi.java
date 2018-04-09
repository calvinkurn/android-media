package com.tokopedia.posapp.product.management.data.source;

import com.tokopedia.posapp.base.data.pojo.PosResponse;
import com.tokopedia.posapp.common.PosUrl;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductListData;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author okasurya on 3/20/18.
 */

public interface ProductManagementApi {
    @GET(PosUrl.Product.PRODUCT_LIST_V2)
    Observable<Response<PosResponse<ProductListData>>> getProducts(@QueryMap Map<String, String> params);

    @POST(PosUrl.Product.EDIT_PRODUCT)
    Observable<Response> editProduct(@Field(ProductConstant.Key.OUTLET_ID) String outletId, @Body String requestBody);
}
