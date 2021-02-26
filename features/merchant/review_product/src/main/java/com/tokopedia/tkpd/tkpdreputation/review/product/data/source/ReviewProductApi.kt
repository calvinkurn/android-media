package com.tokopedia.tkpd.tkpdreputation.review.product.data.source

import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ReviewProductApi {

    @GET(ReputationBaseURL.PATH_GET_REVIEW_PRODUCT_LIST)
    suspend fun getReviewProductList(@QueryMap params: Map<String, String>): Response<DataResponse<DataResponseReviewProduct>>

    @GET(ReputationBaseURL.PATH_GET_REVIEW_HELPFUL_LIST)
    suspend fun getReviewHelpfulList(@QueryMap params: Map<String, String>): Response<DataResponse<DataResponseReviewHelpful>>

    @GET(ReputationBaseURL.PATH_GET_REVIEW_PRODUCT_RATING)
    suspend fun getReviewStarCount(@QueryMap params: Map<String, String>): Response<DataResponse<DataResponseReviewStarCount>>

}