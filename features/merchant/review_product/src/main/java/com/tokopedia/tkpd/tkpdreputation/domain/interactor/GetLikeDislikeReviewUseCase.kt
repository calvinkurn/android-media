package com.tokopedia.tkpd.tkpdreputation.domain.interactor

import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetLikeDislikeReviewUseCase @Inject constructor(
        private val reputationRepository: ReputationRepository
) : UseCase<GetLikeDislikeReviewDomain>() {

    companion object {
        const val PARAM_REVIEW_IDS = "review_ids"
        const val PARAM_USER_ID = "user_id"
    }

    lateinit var params: Params

    override suspend fun executeOnBackground(): GetLikeDislikeReviewDomain {
        if (::params.isInitialized) {
            return reputationRepository.getLikeDislikeReview(mapOf(
                    PARAM_REVIEW_IDS to params.reviewIds,
                    PARAM_USER_ID to params.userId
            ))
        } else throw ErrorMessageException("params not initialized")
    }

    data class Params(
            val reviewIds: String,
            val userId: String
    )

}