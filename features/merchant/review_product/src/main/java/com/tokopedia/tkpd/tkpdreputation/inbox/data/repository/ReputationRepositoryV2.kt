package com.tokopedia.tkpd.tkpdreputation.inbox.data.repository

import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.data.factory.ReputationFactory
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import javax.inject.Inject

class ReputationRepositoryV2 @Inject constructor(
        private val reputationFactory: ReputationFactory
) {

    suspend fun getReviewStarCount(productId: String): DataResponseReviewStarCount {
        return reputationFactory
                .createCloudGetReviewStarCount()
                .getReviewStarCount(productId)
    }

    suspend fun getReviewHelpful(shopId: String, productId: String): DataResponseReviewHelpful {
        return reputationFactory.createCloudGetReviewHelpful()
                .getReviewHelpfulList(shopId, productId)
    }

    suspend fun getLikeDislikeReview(params: Map<String, String>): GetLikeDislikeReviewDomain {
        return reputationFactory.createCloudGetLikeDislikeDataSource().getLikeDislikeReview(params)
    }

    suspend fun getReviewProductList(
            productId: String,
            page: String,
            perPage: String,
            rating: String,
            withAttachment: String
    ): DataResponseReviewProduct {
        return reputationFactory
                .createCloudGetReviewProductList()
                .getReviewProductList(productId, page, perPage, rating, withAttachment)
    }

    suspend fun deleteReviewResponse(params: Map<String, String>): DeleteReviewResponseDomain {
        return reputationFactory.createCloudDeleteReviewResponseDataSource().deleteReviewResponse(params)

    }

    suspend fun likeDislikeReview(params: Map<String, String>): LikeDislikeDomain {
        return reputationFactory.createCloudLikeDislikeDataSource().getLikeDislikeReview(params)
    }

}