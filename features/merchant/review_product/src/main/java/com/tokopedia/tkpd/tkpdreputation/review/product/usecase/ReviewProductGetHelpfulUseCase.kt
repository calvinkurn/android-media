package com.tokopedia.tkpd.tkpdreputation.review.product.usecase

import com.tokopedia.design.utils.StringUtils
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCase
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.Review
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class ReviewProductGetHelpfulUseCase @Inject constructor(
        private val reputationRepository: ReputationRepository,
        private val userSession: UserSession,
        private val getLikeDislikeReviewUseCase: GetLikeDislikeReviewUseCase
) : UseCase<DataResponseReviewHelpful>() {

    lateinit var params: Params

    override suspend fun executeOnBackground(): DataResponseReviewHelpful {
        return reputationRepository.getReviewHelpful(
                userSession.shopId,
                params.productId
        ).let { dataResponseReviewHelpful ->
            if (::params.isInitialized) {
                val reviewList = dataResponseReviewHelpful.list
                if (reviewList != null && reviewList.isNotEmpty()) {
                    getLikeDislikeReviewUseCase.params = GetLikeDislikeReviewUseCase.Params(
                            reviewIds = createReviewIds(reviewList),
                            userId = params.userId
                    )
                    mapLikeModelToReviewModel(getLikeDislikeReviewUseCase.executeOnBackground(), dataResponseReviewHelpful)
                } else dataResponseReviewHelpful
            } else throw ErrorMessageException("params not initialized")
        }
    }

    private fun createReviewIds(reviewList: List<Review>): String {
        val listIds = reviewList.map { it.reviewId.toString() }
        return StringUtils.convertListToStringDelimiter(listIds, "~")
    }

    private fun mapLikeModelToReviewModel(getLikeDislikeReviewDomain: GetLikeDislikeReviewDomain, dataResponseReviewHelpful: DataResponseReviewHelpful): DataResponseReviewHelpful {
        for (review in dataResponseReviewHelpful.list) {
            for (likeDislikeListDomain in getLikeDislikeReviewDomain.list) {
                if (likeDislikeListDomain.reviewId == review.reviewId) {
                    review.totalLike = likeDislikeListDomain.totalLike
                    review.likeStatus = likeDislikeListDomain.likeStatus
                    break
                }
            }
        }
        return dataResponseReviewHelpful
    }

    data class Params(
            val productId: String,
            val userId: String
    )
}