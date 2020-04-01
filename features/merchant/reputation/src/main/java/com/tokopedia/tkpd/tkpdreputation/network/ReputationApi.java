package com.tokopedia.tkpd.tkpdreputation.network;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface ReputationApi {

    @GET(ReputationBaseURL.PATH_GET_INBOX_REPUTATION)
    Observable<Response<TokopediaWsV4Response>> getInbox(@QueryMap Map<String, String> params);

    @GET(ReputationBaseURL.PATH_GET_DETAIL_INBOX_REPUTATION)
    Observable<Response<TokopediaWsV4Response>> getInboxDetail(@QueryMap Map<String, String> params);

    @POST(ReputationBaseURL.PATH_SEND_REPUTATION_SMILEY)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> sendSmiley(@FieldMap Map<String, String> params);

    @POST(ReputationBaseURL.PATH_SEND_REVIEW_VALIDATE)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> sendReviewValidate(@FieldMap Map<String, String> params);

    @POST(ReputationBaseURL.PATH_SEND_REVIEW_SUBMIT)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> sendReviewSubmit(@FieldMap Map<String, String> params);

    @POST(ReputationBaseURL.PATH_SKIP_REVIEW)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> skipReview(@FieldMap Map<String, String> params);

    @POST(ReputationBaseURL.PATH_EDIT_REVIEW_VALIDATE)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> editReviewValidate(@FieldMap Map<String, String> params);

    @POST(ReputationBaseURL.PATH_EDIT_REVIEW_SUBMIT)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> editReviewSubmit(@FieldMap Map<String, String> params);

    @POST(ReputationBaseURL.PATH_REPORT_REVIEW)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> reportReview(@FieldMap Map<String, String> params);

    @POST(ReputationBaseURL.PATH_DELETE_REVIEW_RESPONSE)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> deleteReviewResponse(@FieldMap Map<String, String> params);

    @POST(ReputationBaseURL.PATH_INSERT_REVIEW_RESPONSE)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> insertReviewResponse(@FieldMap Map<String, String> params);

    @GET(ReputationBaseURL.PATH_GET_LIKE_DISLIKE_REVIEW)
    Observable<Response<TokopediaWsV4Response>> getLikeDislikeReview(@QueryMap Map<String, Object>
                                                                    parameters);

    @POST(ReputationBaseURL.PATH_LIKE_DISLIKE_REVIEW)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> likeDislikeReview(@FieldMap Map<String, String> params);

}
