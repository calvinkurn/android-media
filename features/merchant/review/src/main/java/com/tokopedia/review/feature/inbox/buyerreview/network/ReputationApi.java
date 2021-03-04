package com.tokopedia.review.feature.inbox.buyerreview.network;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;

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

    @POST(ReputationBaseURL.PATH_REPORT_REVIEW)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> reportReview(@FieldMap Map<String, String> params);

    @POST(ReputationBaseURL.PATH_INSERT_REVIEW_RESPONSE)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> insertReviewResponse(@FieldMap Map<String, String> params);

    @POST(ReputationBaseURL.PATH_DELETE_REVIEW_RESPONSE)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> deleteReviewResponse(@FieldMap Map<String, String> params);

}
