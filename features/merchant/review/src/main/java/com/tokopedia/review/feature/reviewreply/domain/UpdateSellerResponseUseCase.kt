package com.tokopedia.review.feature.reviewreply.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.reviewreply.data.ReviewReplyUpdateResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class UpdateSellerResponseUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
): UseCase<ReviewReplyUpdateResponse.ProductrevUpdateSellerResponse>() {

    companion object {
        private const val FEEDBACK_ID = "feedbackID"
        private const val RESPONSE_MESSAGE = "responseMessage"
        const val UPDATE_REVIEW_REPLY_MUTATION_CLASS_NAME = "UpdateReviewReply"
        const val UPDATE_REVIEW_REPLY_MUTATION = """
            mutation update_review_reply(${'$'}feedbackID: Int!, ${'$'}responseMessage: String!) {
                productrevUpdateSellerResponse(feedbackID: ${'$'}feedbackID, responseMessage: ${'$'}responseMessage) {
                    success
                    data {
                      shopID
                      feedbackID
                      responseBy
                      responseMessage
                    }
                  }
            }
        """

        @JvmStatic
        fun createParams(productId: Int, responseMessage: String): Map<String, Any> =
                mapOf(FEEDBACK_ID to productId, RESPONSE_MESSAGE to responseMessage)
    }

    var params = mapOf<String, Any>()

    @GqlQuery(UPDATE_REVIEW_REPLY_MUTATION_CLASS_NAME, UPDATE_REVIEW_REPLY_MUTATION)
    override suspend fun executeOnBackground(): ReviewReplyUpdateResponse.ProductrevUpdateSellerResponse {
        val gqlRequest = GraphqlRequest(UpdateReviewReply.GQL_QUERY, ReviewReplyUpdateResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error.isNullOrEmpty()) {
            return gqlResponse.getData<ReviewReplyUpdateResponse>(ReviewReplyUpdateResponse::class.java).productrevUpdateSellerResponse
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message} )
        }
    }
}