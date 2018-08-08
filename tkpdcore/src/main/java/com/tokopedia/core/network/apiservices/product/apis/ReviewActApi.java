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
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public interface ReviewActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_ADD_COMMENT_REVIEW)
    Observable<Response<TkpdResponse>> addCommentReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_ADD_REVIEW)
    Observable<Response<TkpdResponse>> addProductReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_DELETE_COMMENT_REVIEW)
    Observable<Response<TkpdResponse>> deleteCommentReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_LIKE_DISLIKE_REVIEW)
    Observable<Response<TkpdResponse>> likeDislikeReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_REPORT_REVIEW)
    Observable<Response<TkpdResponse>> reportReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_SET_READ_REVIEW)
    Observable<Response<TkpdResponse>> setReadReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_SKIP_REVIEW)
    Observable<Response<TkpdResponse>> skipProductReview(@FieldMap Map<String, String> params);

}
