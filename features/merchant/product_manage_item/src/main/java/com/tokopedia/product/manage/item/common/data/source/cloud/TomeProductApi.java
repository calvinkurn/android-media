package com.tokopedia.product.manage.item.common.data.source.cloud;

import com.tokopedia.product.manage.item.common.util.ProductUrl;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprdold.ProductVariantByPrdModel;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hendry on 24/05/17.
 */

public interface TomeProductApi {

    @Headers({"Content-Type: application/json"})
    @POST(ProductUrl.URL_ADD_PRODUCT)
    Observable<Response<DataResponse<ProductSubmitResp>>> addProductSubmit(@Body String productViewModel);

    @Headers({"Content-Type: application/json"})
    @PATCH(ProductUrl.URL_ADD_PRODUCT + "/{" + ProductUrl.PRODUCT_ID + "}")
    Observable<Response<DataResponse<ProductSubmitResp>>> editProductSubmit(@Path(ProductUrl.PRODUCT_ID) String productId, @Body String productViewModel);

    @GET(ProductUrl.URL_ADD_PRODUCT + "/{" + ProductUrl.PRODUCT_ID + "}")
    Observable<Response<DataResponse<ProductViewModel>>> getProductDetail(@Path(ProductUrl.PRODUCT_ID) String productId, @Query("show_variant") int showVariant);

    @GET(ProductUrl.GET_VARIANT_BY_CAT_PATH)
    Observable<Response<DataResponse<List<ProductVariantByCatModel>>>> getProductVariantByCat(@Query(ProductUrl.CAT_ID) long categoryId);

    @GET(ProductUrl.GET_VARIANT_BY_PRD_PATH)
    Observable<Response<DataResponse<ProductVariantByPrdModel>>> getProductVariantByPrd(@Query(ProductUrl.PRD_ID) long productId);

}
