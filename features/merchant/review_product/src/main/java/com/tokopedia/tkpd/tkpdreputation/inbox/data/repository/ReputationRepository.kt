package com.tokopedia.tkpd.tkpdreputation.inbox.data.repository

import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.data.factory.ReputationFactory
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ReputationRepository @Inject constructor(
        private val reputationFactory: ReputationFactory
) {

    suspend fun getReviewStarCount(params: RequestParams): DataResponseReviewStarCount {
        return reputationFactory
                .createCloudGetReviewStarCount()
                .getReviewStarCount(params)
    }

    suspend fun getReviewHelpful(params: RequestParams): DataResponseReviewHelpful {
        return reputationFactory.createCloudGetReviewHelpful()
                .getReviewHelpfulList(params)
    }

    suspend fun getLikeDislikeReview(params: RequestParams): GetLikeDislikeReviewDomain {
        return reputationFactory.createCloudGetLikeDislikeDataSource().getLikeDislikeReview(params)
    }

    suspend fun getReviewProductList(params: RequestParams): DataResponseReviewProduct {
        return reputationFactory
                .createCloudGetReviewProductList()
                .getReviewProductList(params)
    }

    suspend fun deleteReviewResponse(params: RequestParams): DeleteReviewResponseDomain {
        return reputationFactory.createCloudDeleteReviewResponseDataSource().deleteReviewResponse(params)

    }

    suspend fun likeDislikeReview(params: RequestParams): LikeDislikeDomain {
        return reputationFactory.createCloudLikeDislikeDataSource().getLikeDislikeReview(params)
    }

}