package com.tokopedia.core.network.apiservices.product.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * ProductActApi
 * Created by Angga.Prasetiyo on 04/12/2015.
 */

@Deprecated
public interface ProductActApi {


    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_ETALASE)
    Observable<Response<TkpdResponse>> editEtalase(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_MOVE_TO_WAREHOUSE)
    Observable<Response<TkpdResponse>> moveToWarehouse(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_PROMOTE_PRODUCT)
    Observable<Response<TkpdResponse>> promote(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_REPORT_PRODUCT)
    Observable<Response<TkpdResponse>> report(@FieldMap Map<String, String> params);


}
