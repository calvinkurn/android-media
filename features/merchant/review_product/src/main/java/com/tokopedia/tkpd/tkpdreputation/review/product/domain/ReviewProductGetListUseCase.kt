package com.tokopedia.tkpd.tkpdreputation.review.product.domain

import com.tokopedia.design.utils.StringUtils
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCase
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ReviewProductGetListUseCase @Inject constructor(
        private val reputationRepository: ReputationRepository,
        private val getLikeDislikeReviewUseCase: GetLikeDislikeReviewUseCase
) : UseCase<DataResponseReviewProduct>() {

    companion object {
        private const val DEFAULT_NO_ATTACHMENT = "0"
        private const val WITH_ATTACHMENT_IMAGE_VALUE = "1"
        private const val DEFAULT_PER_PAGE = "10"
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_PAGE = "page"
        private const val PARAM_PER_PAGE = "per_page"
        private const val PARAM_RATING = "rating"
        private const val PARAM_USER_ID = "user_id"
        private const val PARAM_WITH_ATTACHMENT = "with_attachment"
    }

    private val params = RequestParams.create()

    override suspend fun executeOnBackground(): DataResponseReviewProduct {
        val reviewProductListParams = RequestParams.create().apply {
            putString(PARAM_PRODUCT_ID, params.getString(PARAM_PRODUCT_ID, ""))
            putString(PARAM_PAGE, params.getString(PARAM_PAGE, ""))
            putString(PARAM_PER_PAGE, params.getString(PARAM_PER_PAGE, DEFAULT_PER_PAGE))
            putString(PARAM_RATING, params.getString(PARAM_RATING, ""))
            val withAttachment = if (params.getBoolean(PARAM_WITH_ATTACHMENT, false)) {
                WITH_ATTACHMENT_IMAGE_VALUE
            } else DEFAULT_NO_ATTACHMENT
            putString(PARAM_WITH_ATTACHMENT, withAttachment)
        }
        reputationRepository.getReviewProductList(reviewProductListParams).let { dataResponseReviewProduct ->
            return if (dataResponseReviewProduct.list != null && dataResponseReviewProduct.list.isNotEmpty()) {
                getLikeDislikeReviewUseCase.setParams(createReviewIds(dataResponseReviewProduct), params.getString(PARAM_USER_ID, ""))
                mapLikeModelToReview(getLikeDislikeReviewUseCase.executeOnBackground(), dataResponseReviewProduct)
            } else dataResponseReviewProduct
        }
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

    fun setParams(
            productId: String,
            page: String,
            perPage: String = DEFAULT_PER_PAGE,
            rating: String,
            userId: String,
            withAttachment: Boolean
    ) {
        params.putString(PARAM_PRODUCT_ID, productId)
        params.putString(PARAM_PAGE, page)
        params.putString(PARAM_PER_PAGE, perPage)
        params.putString(PARAM_RATING, rating)
        params.putString(PARAM_USER_ID, userId)
        params.putBoolean(PARAM_WITH_ATTACHMENT, withAttachment)
    }
}