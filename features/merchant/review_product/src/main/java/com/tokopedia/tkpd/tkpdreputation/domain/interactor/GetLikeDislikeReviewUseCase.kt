package com.tokopedia.tkpd.tkpdreputation.domain.interactor

import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetLikeDislikeReviewUseCase @Inject constructor(
        private val reputationRepository: ReputationRepository
) : UseCase<GetLikeDislikeReviewDomain>() {

    companion object {
        private const val PARAM_REVIEW_IDS = "review_ids"
        private const val PARAM_USER_ID = "user_id"
    }

    private val params = RequestParams.create()

    override suspend fun executeOnBackground(): GetLikeDislikeReviewDomain {
        return reputationRepository.getLikeDislikeReview(params)
    }

    fun setParams(reviewIds: String, userId: String) {
        params.putString(PARAM_REVIEW_IDS, reviewIds)
        params.putString(PARAM_USER_ID, userId)
    }
}