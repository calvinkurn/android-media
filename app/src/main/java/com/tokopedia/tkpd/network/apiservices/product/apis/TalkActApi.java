package com.tokopedia.tkpd.network.apiservices.product.apis;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public interface TalkActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_ADD_COMMENT_TALK)
    Observable<Response<TkpdResponse>> addCommentTalk(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_ADD_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> addProductTalk(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_DELETE_COMMENT_TALK)
    Observable<Response<TkpdResponse>> deleteCommentTalk(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_DELETE_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> deleteProductTalk(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_FOLLOW_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> followProductTalk(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_REPORT_COMMENT_TALK)
    Observable<Response<TkpdResponse>> reportCommentTalk(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_REPORT_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> reportProductTalk(@FieldMap Map<String, String> params);

}
