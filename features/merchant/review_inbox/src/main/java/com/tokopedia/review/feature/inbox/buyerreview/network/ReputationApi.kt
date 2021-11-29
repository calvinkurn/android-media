package com.tokopedia.review.feature.inbox.buyerreview.network

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

interface ReputationApi {
    @GET(ReputationBaseURL.PATH_GET_INBOX_REPUTATION)
    fun getInbox(@QueryMap params: Map<String, String>): Observable<Response<TokopediaWsV4Response?>?>

    @GET(ReputationBaseURL.PATH_GET_DETAIL_INBOX_REPUTATION)
    fun getInboxDetail(@QueryMap params: Map<String, String>): Observable<Response<TokopediaWsV4Response?>?>

    @POST(ReputationBaseURL.PATH_SEND_REPUTATION_SMILEY)
    @FormUrlEncoded
    fun sendSmiley(@FieldMap params: Map<String, String>): Observable<Response<TokopediaWsV4Response?>?>

    @POST(ReputationBaseURL.PATH_REPORT_REVIEW)
    @FormUrlEncoded
    fun reportReview(@FieldMap params: Map<String, String>): Observable<Response<TokopediaWsV4Response?>?>

    @POST(ReputationBaseURL.PATH_INSERT_REVIEW_RESPONSE)
    @FormUrlEncoded
    fun insertReviewResponse(@FieldMap params: Map<String, String>): Observable<Response<TokopediaWsV4Response?>?>

    @POST(ReputationBaseURL.PATH_DELETE_REVIEW_RESPONSE)
    @FormUrlEncoded
    fun deleteReviewResponse(@FieldMap params: Map<String, String>): Observable<Response<TokopediaWsV4Response?>?>
}