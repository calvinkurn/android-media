package com.tokopedia.core.network.apiservices.shop.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public interface ReputationActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_DELETE_REP_REVIEW_RESPONSE)
    Observable<Response<TkpdResponse>> deleteRepReviewResponse(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_EDIT_REP_REVIEW)
    Observable<Response<TkpdResponse>> editRepReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_INSERT_REP)
    Observable<Response<TkpdResponse>> insertRep(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_INSERT_REP_REVIEW)
    Observable<Response<TkpdResponse>> insertRepReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_INSERT_REP_REVIEW_RESPONSE)
    Observable<Response<TkpdResponse>> insertRepReviewResponse(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_SKIP_REP_REVIEW)
    Observable<Response<TkpdResponse>> skipRepReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_REPUTATION_REVIEW_VALIDATION)
    Observable<Response<TkpdResponse>> editProductReviewValidation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_REPUTATION_REVIEW_SUBMIT)
    Observable<Response<TkpdResponse>> editProductReviewSubmit(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_ADD_REPUTATION_REVIEW_VALIDATION)
    Observable<Response<TkpdResponse>> insertReviewValidation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_ADD_REPUTATION_REVIEW_SUBMIT)
    Observable<Response<TkpdResponse>> insertReviewSubmit(@FieldMap Map<String, String> params);

}
