package com.tokopedia.product.manage.list.data.source;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.product.manage.item.common.data.source.cloud.DataResponse;
import com.tokopedia.product.manage.list.data.model.ResponseDeleteProductData;
import com.tokopedia.product.manage.list.data.model.ResponseEditPriceData;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ZulfikarRahman on 20/9/17.
 */

public interface ProductActionApi {
    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.V4_ACTION_PRODUCT + TkpdBaseURL.Product.PATH_DELETE_PRODUCT)
    Observable<Response<DataResponse<ResponseDeleteProductData>>> delete(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.V4_ACTION_PRODUCT + TkpdBaseURL.Product.PATH_EDIT_PRICE)
    Observable<Response<DataResponse<ResponseEditPriceData>>> editPrice(@FieldMap Map<String, String> params);
}