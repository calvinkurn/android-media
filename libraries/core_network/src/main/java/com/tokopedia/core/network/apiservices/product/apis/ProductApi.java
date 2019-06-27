package com.tokopedia.core.network.apiservices.product.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * ProductApi
 * Created by Angga.Prasetiyo on 25/11/2015.
 */

@Deprecated
public interface ProductApi {

    @GET(TkpdBaseURL.Product.PATH_GET_DETAIL_PRODUCT)
    Observable<Response<TkpdResponse>> getProductDetail(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_GET_OTHER_PRODUCT)
    Observable<Response<TkpdResponse>> getOtherProducts(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_GET_ADD_PRODUCT_FORM)
    Observable<Response<TkpdResponse>> getAddForm(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_GET_EDIT_PRODUCT_FORM)
    Observable<Response<TkpdResponse>> getEditForm(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_GET_LIKE_REVIEW)
    Observable<Response<TkpdResponse>> getLikeReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_GET_PICTURE_PRODUCT)
    Observable<Response<TkpdResponse>> getPictures(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Product.PATH_GET_REVIEW)
    Observable<Response<TkpdResponse>> getReview(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Product.PATH_GET_HELPFUL_REVIEW)
    Observable<Response<TkpdResponse>> getHelpfulReview(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Product.PATH_GET_TALK)
    Observable<Response<TkpdResponse>> getTalk(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Product.PATH_MANAGE_PRODUCT)
    Observable<Response<TkpdResponse>> manage(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Product.PATH_GET_REPORT_PRODUCT_TYPE)
    Observable<Response<TkpdResponse>> getProductReportType(@QueryMap Map<String, String> params);

}
