package com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ToggleLikeReviewResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ToggleLikeReviewUseCase @Inject constructor(
    @ApplicationContext graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ToggleLikeReviewResponse>(graphqlRepository) {

    companion object {
        const val LIKED = 1
        const val NEUTRAL = 3
        const val PARAM_FEEDBACK_ID = "feedbackID"
        const val PARAM_LIKE_STATUS = "likeStatus"
        const val TOGGLE_LIKE_REVIEW_MUTATION = """
            mutation toggleLikeDislike(${'$'}feedbackID: String!, ${'$'}likeStatus: Int!) {
              productrevLikeReview(feedbackID: ${'$'}feedbackID, likeStatus: ${'$'}likeStatus) {
                feedbackID
                likeStatus
                totalLike
                totalDislike
              }
            }
        """
    }

    init {
        setTypeClass(ToggleLikeReviewResponse::class.java)
        setGraphqlQuery(TOGGLE_LIKE_REVIEW_MUTATION)
    }

    private val requestParams = RequestParams.create()

    fun setParams(reviewId: String, likeStatus: Int) {
        requestParams.apply {
            putString(PARAM_FEEDBACK_ID, reviewId)
            putInt(PARAM_LIKE_STATUS, likeStatus)
        }
        setRequestParams(requestParams.parameters)
    }
}