package com.tokopedia.tkpd.tkpdreputation.review.product.usecase

import com.tokopedia.design.utils.StringUtils
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCase
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.Review
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class ReviewProductGetHelpfulUseCase @Inject constructor(
        private val reputationRepository: ReputationRepository,
        private val userSession: UserSession,
        private val getLikeDislikeReviewUseCase: GetLikeDislikeReviewUseCase
) : UseCase<DataResponseReviewHelpful>() {

    companion object {
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_USER_ID = "user_id"
        private const val PARAM_SHOP_ID = "shop_id"
    }

    private val params = RequestParams.create()

    override suspend fun executeOnBackground(): DataResponseReviewHelpful {
        val reviewHelpfulParams = RequestParams.create().apply {
            putString(PARAM_PRODUCT_ID, params.getString(PARAM_PRODUCT_ID, ""))
            putString(PARAM_SHOP_ID, userSession.shopId)
        }
        return reputationRepository.getReviewHelpful(reviewHelpfulParams).let { dataResponseReviewHelpful ->
            val reviewList = dataResponseReviewHelpful.list
            if (reviewList != null && reviewList.isNotEmpty()) {
                getLikeDislikeReviewUseCase.setParams(
                        createReviewIds(reviewList),
                        params.getString(PARAM_USER_ID, "")
                )
                mapLikeModelToReviewModel(
                        getLikeDislikeReviewUseCase.executeOnBackground(),
                        dataResponseReviewHelpful
                )
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
                    break
                }
            }
        }
        return dataResponseReviewHelpful
    }

    fun setParams(productId: String, userId: String) {
        params.putString(PARAM_PRODUCT_ID, productId)
        params.putString(PARAM_USER_ID, userId)
    }
}