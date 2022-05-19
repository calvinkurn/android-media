package com.tokopedia.review.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.common.data.ToggleLikeReviewResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(ToggleLikeReviewUseCase.TOGGLE_LIKE_REVIEW_MUTATION_CLASS_NAME, ToggleLikeReviewUseCase.TOGGLE_LIKE_REVIEW_MUTATION)
class ToggleLikeReviewUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ToggleLikeReviewResponse>(graphqlRepository) {

    companion object {
        const val PARAM_FEEDBACK_ID = "feedbackID"
        const val PARAM_LIKE_STATUS = "likeStatus"
        const val TOGGLE_LIKE_REVIEW_MUTATION_CLASS_NAME = "ToggleLikeReviewMutation"
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
        setGraphqlQuery(ToggleLikeReviewMutation.GQL_QUERY)
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