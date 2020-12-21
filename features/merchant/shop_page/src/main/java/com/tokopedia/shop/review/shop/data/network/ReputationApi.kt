package com.tokopedia.shop.review.shop.data.network

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.shop.review.constant.ReputationBaseURL
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

@JvmSuppressWildcards
interface ReputationApi {
    @POST(ReputationBaseURL.PATH_DELETE_REVIEW_RESPONSE)
    @FormUrlEncoded
    fun deleteReviewResponse(@FieldMap params: Map<String?, String?>?): Observable<Response<TokopediaWsV4Response?>?>?

    @GET(ReputationBaseURL.PATH_GET_LIKE_DISLIKE_REVIEW)
    fun getLikeDislikeReview(@QueryMap parameters: Map<String?, Any?>?): Observable<Response<TokopediaWsV4Response?>?>?

    @POST(ReputationBaseURL.PATH_LIKE_DISLIKE_REVIEW)
    @FormUrlEncoded
    fun likeDislikeReview(@FieldMap params: Map<String?, String?>?): Observable<Response<TokopediaWsV4Response?>?>?
}