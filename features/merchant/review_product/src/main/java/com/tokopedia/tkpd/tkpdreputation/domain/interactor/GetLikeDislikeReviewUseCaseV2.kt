package com.tokopedia.tkpd.tkpdreputation.domain.interactor

import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepositoryV2
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetLikeDislikeReviewUseCaseV2 @Inject constructor(
        private val reputationRepository: ReputationRepositoryV2
) : UseCase<GetLikeDislikeReviewDomain>() {

    companion object {
        const val PARAM_REVIEW_IDS = "review_ids"
        const val PARAM_USER_ID = "user_id"
    }

    val params = hashMapOf<String, String>()

    override suspend fun executeOnBackground(): GetLikeDislikeReviewDomain {
        return reputationRepository.getLikeDislikeReview(params)
    }

}