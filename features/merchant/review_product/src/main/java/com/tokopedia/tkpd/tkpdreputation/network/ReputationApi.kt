package com.tokopedia.tkpd.tkpdreputation.network

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL
import retrofit2.Response
import retrofit2.http.*

interface ReputationApi {

    @POST(ReputationBaseURL.PATH_DELETE_REVIEW_RESPONSE)
    @FormUrlEncoded
    suspend fun deleteReviewResponse(@FieldMap params: Map<String, String>): Response<TokopediaWsV4Response>

    @GET(ReputationBaseURL.PATH_GET_LIKE_DISLIKE_REVIEW)
    suspend fun getLikeDislikeReview(@QueryMap parameters: Map<String, String>): Response<TokopediaWsV4Response>

    @POST(ReputationBaseURL.PATH_LIKE_DISLIKE_REVIEW)
    @FormUrlEncoded
    suspend fun likeDislikeReview(@FieldMap params: Map<String, String>): Response<TokopediaWsV4Response>
}