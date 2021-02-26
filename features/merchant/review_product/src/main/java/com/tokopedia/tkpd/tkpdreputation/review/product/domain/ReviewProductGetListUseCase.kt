package com.tokopedia.tkpd.tkpdreputation.review.product.domain

import com.tokopedia.design.utils.StringUtils
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCase
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ReviewProductGetListUseCase @Inject constructor(
        private val reputationRepository: ReputationRepository,
        private val getLikeDislikeReviewUseCase: GetLikeDislikeReviewUseCase
) : UseCase<DataResponseReviewProduct>() {

    companion object {
        const val USER_ID = "user_id"
        const val DEFAULT_NO_ATTACHMENT = "0"
        const val WITH_ATTACHMENT_IMAGE_VALUE = "1"
        const val DEFAULT_PER_PAGE = "10"
    }

    lateinit var params: Params

    override suspend fun executeOnBackground(): DataResponseReviewProduct {
        if (::params.isInitialized) {
            return reputationRepository.getReviewProductList(
                    params.productId,
                    params.page,
                    params.perPage,
                    params.rating,
                    if (params.withAttachment) WITH_ATTACHMENT_IMAGE_VALUE
                    else DEFAULT_NO_ATTACHMENT
            ).let { dataResponseReviewProduct ->
                return if (dataResponseReviewProduct.list != null && dataResponseReviewProduct.list.isNotEmpty()) {
                    getLikeDislikeReviewUseCase.params = GetLikeDislikeReviewUseCase.Params(
                            reviewIds = createReviewIds(dataResponseReviewProduct),
                            userId = params.userId
                    )
                    mapLikeModelToReview(getLikeDislikeReviewUseCase.executeOnBackground(), dataResponseReviewProduct)
                } else dataResponseReviewProduct
            }
        } else throw ErrorMessageException("params not initialized")
    }

    private fun createReviewIds(dataResponseReviewProduct: DataResponseReviewProduct): String {
        val listIds = dataResponseReviewProduct.list.map { review ->
            review.reviewId.toString()
        }
        return StringUtils.convertListToStringDelimiter(listIds, "~")
    }

    private fun mapLikeModelToReview(getLikeDislikeReviewDomain: GetLikeDislikeReviewDomain, dataResponseReviewProduct: DataResponseReviewProduct): DataResponseReviewProduct {
        for (review in dataResponseReviewProduct.list) {
            for (likeDislikeListDomain in getLikeDislikeReviewDomain.list) {
                if (likeDislikeListDomain.reviewId == review.reviewId) {
                    review.totalLike = likeDislikeListDomain.totalLike
                    review.likeStatus = likeDislikeListDomain.likeStatus
                    break
                }
            }
        }
        return dataResponseReviewProduct
    }

    data class Params(
            val productId: String,
            val page: String,
            val perPage: String = DEFAULT_PER_PAGE,
            val rating: String,
            val userId: String,
            val withAttachment: Boolean
    )
}