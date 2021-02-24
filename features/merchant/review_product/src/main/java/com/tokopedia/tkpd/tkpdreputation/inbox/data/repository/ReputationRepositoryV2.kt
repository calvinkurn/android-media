package com.tokopedia.tkpd.tkpdreputation.inbox.data.repository

import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.data.factory.ReputationFactory
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import javax.inject.Inject

class ReputationRepositoryV2 @Inject constructor(
        private val reputationFactory: ReputationFactory
) {

    suspend fun getReviewStarCount(productId: String): DataResponseReviewStarCount {
        return reputationFactory
                .createCloudGetReviewStarCountV2()
                .getReviewStarCount(productId)
    }

    suspend fun getReviewHelpful(shopId: String, productId: String): DataResponseReviewHelpful {
        return reputationFactory.createCloudGetReviewHelpfulV2()
                .getReviewHelpfulList(shopId, productId)
    }

    suspend fun getLikeDislikeReview(params: Map<String, String>): GetLikeDislikeReviewDomain {
        return reputationFactory.createCloudGetLikeDislikeDataSourceV2().getLikeDislikeReview(params)
    }

}