package com.tokopedia.shop.review.shop.domain

import com.tokopedia.shop.review.shop.domain.model.LikeDislikeDomain
import com.tokopedia.shop.review.shop.domain.repository.ReputationRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 9/29/17.
 * https://phab.tokopedia.com/w/api/jerry/post-like-dislike-review/
 */
class LikeDislikeReviewUseCase(private val reputationRepository: ReputationRepository) : UseCase<LikeDislikeDomain>() {
    override fun createObservable(requestParams: RequestParams): Observable<LikeDislikeDomain> {
        return reputationRepository.likeDislikeReview(requestParams)
    }

    companion object {
        private const val PARAM_REVIEW_ID = "review_id"
        private const val PARAM_LIKE_STATUS = "like_status"
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_SHOP_ID = "shop_id"
        const val DEFAULT_NOT_LIKED = 0
        const val STATUS_LIKE = 1
        const val STATUS_DISLIKE = 2
        const val STATUS_RESET = 3
        private const val PARAM_ACTION = "action"
        private const val ACTION_LIKE_DISLIKE_REVIEW = "event_like_dislike_review"
        fun getParam(reviewId: String?, likeStatus: Int,
                     productId: String?, shopId: String?): RequestParams {
            val params = RequestParams.create()
            params.putString(PARAM_REVIEW_ID, reviewId)
            params.putInt(PARAM_LIKE_STATUS, likeStatus)
            params.putString(PARAM_PRODUCT_ID, productId)
            params.putString(PARAM_SHOP_ID, shopId)
            params.putString(PARAM_ACTION, ACTION_LIKE_DISLIKE_REVIEW)
            return params
        }
    }

}