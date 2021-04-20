package com.tokopedia.shop.review.shop.domain

import com.tokopedia.shop.review.shop.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.shop.review.shop.domain.repository.ReputationRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 9/29/17.
 */
class GetLikeDislikeReviewUseCase(private val reputationRepository: ReputationRepository) : UseCase<GetLikeDislikeReviewDomain>() {
    override fun createObservable(requestParams: RequestParams): Observable<GetLikeDislikeReviewDomain> {
        return reputationRepository.getLikeDislikeReview(requestParams)
    }

    companion object {
        private const val PARAM_REVIEW_IDS = "review_ids"
        private const val PARAM_USER_ID = "user_id"
        fun getParam(reviewIds: String?, userId: String?): RequestParams {
            val params = RequestParams.create()
            params.putString(PARAM_REVIEW_IDS, reviewIds)
            params.putString(PARAM_USER_ID, userId)
            return params
        }
    }

}