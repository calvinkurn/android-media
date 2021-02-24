package com.tokopedia.tkpd.tkpdreputation.review.product.usecase

import com.tokopedia.design.utils.StringUtils
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCaseV2
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCaseV2.Companion.PARAM_REVIEW_IDS
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCaseV2.Companion.PARAM_USER_ID
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepositoryV2
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.Review
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class ReviewProductGetHelpfulUseCaseV2 @Inject constructor(
        private val reputationRepository: ReputationRepositoryV2,
        private val userSession: UserSession,
        private val getLikeDislikeReviewUseCase: GetLikeDislikeReviewUseCaseV2
) : UseCase<DataResponseReviewHelpful>() {

    companion object {
        const val PRODUCT_ID = "product_id"
        const val USER_ID = "user_id"
    }

    var params = hashMapOf<String, String>()

    override suspend fun executeOnBackground(): DataResponseReviewHelpful {
        return reputationRepository.getReviewHelpful(
                userSession.shopId,
                params[PRODUCT_ID] ?: ""
        ).let { dataResponseReviewHelpful ->
            val reviewList = dataResponseReviewHelpful.list
            if (reviewList != null && reviewList.isNotEmpty()) {
                getLikeDislikeReviewUseCase.params[PARAM_REVIEW_IDS] = createReviewIds(reviewList)
                getLikeDislikeReviewUseCase.params[PARAM_USER_ID] = params[USER_ID] ?: ""
                mapLikeModelToReviewModel(getLikeDislikeReviewUseCase.executeOnBackground(), dataResponseReviewHelpful)
            } else dataResponseReviewHelpful
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
                }
            }
        }
        return dataResponseReviewHelpful
    }
}