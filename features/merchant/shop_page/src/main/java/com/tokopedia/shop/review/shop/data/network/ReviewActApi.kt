package com.tokopedia.shop.review.shop.data.network

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.shop.review.constant.ReputationBaseURL
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable

interface ReviewActApi {
    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_LIKE_DISLIKE_REVIEW_PRODUCT)
    fun likeDislikeReview(@FieldMap params: Map<String?, String?>?): Observable<Response<TokopediaWsV4Response?>?>?

    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_REPORT_REVIEW_PRODUCT)
    fun reportReview(@FieldMap params: Map<String?, String?>?): Observable<Response<TokopediaWsV4Response?>?>?
}