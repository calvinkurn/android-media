package com.tokopedia.review.feature.reviewreply.insert.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.reviewreply.insert.domain.model.ReviewReplyInsertResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ReviewReplyInsertUseCase @Inject constructor(
    private val graphQlRepository: GraphqlRepository
) : UseCase<ReviewReplyInsertResponse.ProductrevInsertSellerResponse>() {

    companion object {
        private const val FEEDBACK_ID = "feedbackID"
        private const val RESPONSE_MESSAGE = "responseMessage"
        const val INSERT_REVIEW_MUTATION_CLASS_NAME = "InsertReview"
        const val INSERT_REVIEW_MUTATION = """
            mutation insert_review_reply(${'$'}feedbackID: String!, ${'$'}responseMessage: String!) {
                  productrevInsertSellerResponse(feedbackID: ${'$'}feedbackID, responseMessage: ${'$'}responseMessage) {
                        success
                  }
            }
        """

        @JvmStatic
        fun createParams(feedbackId: String, responseMessage: String): Map<String, Any> =
            mapOf(FEEDBACK_ID to feedbackId, RESPONSE_MESSAGE to responseMessage)
    }

    var params = mapOf<String, Any>()

    @GqlQuery(INSERT_REVIEW_MUTATION_CLASS_NAME, INSERT_REVIEW_MUTATION)
    override suspend fun executeOnBackground(): ReviewReplyInsertResponse.ProductrevInsertSellerResponse {
        val gqlRequest = GraphqlRequest(InsertReview.GQL_QUERY, ReviewReplyInsertResponse::class.java, params)
        val gqlResponse = graphQlRepository.response(listOf(gqlRequest))
        val error = gqlResponse.getError(ReviewReplyInsertResponse::class.java)
        if (error.isNullOrEmpty()) {
            return gqlResponse.getData<ReviewReplyInsertResponse>(ReviewReplyInsertResponse::class.java).productrevInsertSellerResponse
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }
}