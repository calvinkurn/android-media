package com.tokopedia.review.feature.reviewreply.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.reviewreply.data.ReviewReplyInsertResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class InsertSellerResponseUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
): UseCase<ReviewReplyInsertResponse.InboxReviewInsertReviewResponse>() {

    companion object {
        private const val SHOP_ID = "shopId"
        private const val REVIEW_ID = "reviewId"
        private const val PRODUCT_ID = "productId"
        private const val RESPONSE_MESSAGE = "responseMessage"
        const val INSERT_REVIEW_MUTATION_CLASS_NAME = "InsertReview"
        const val INSERT_REVIEW_MUTATION = """
            mutation insert_review_reply(${'$'}reviewId: Int!, ${'$'}productId: Int!, ${'$'}shopId: Int!, ${'$'}responseMessage: String!) {
                  inboxReviewInsertReviewResponse(reviewId: ${'$'}reviewId, productId: ${'$'}productId, shopId: ${'$'}shopId,
                    responseMessage: ${'$'}responseMessage) {
                        isSuccesss
                  }
            }
        """

        @JvmStatic
        fun createParams(reviewId: Int, productId: Int, shopId: Int, responseMessage: String): Map<String, Any> =
                mapOf(REVIEW_ID to reviewId, PRODUCT_ID to productId, SHOP_ID to shopId, RESPONSE_MESSAGE to responseMessage)
    }

    var params = mapOf<String, Any>()

    @GqlQuery(INSERT_REVIEW_MUTATION_CLASS_NAME, INSERT_REVIEW_MUTATION)
    override suspend fun executeOnBackground(): ReviewReplyInsertResponse.InboxReviewInsertReviewResponse {
        val gqlRequest = GraphqlRequest(InsertReview.GQL_QUERY, ReviewReplyInsertResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error.isNullOrEmpty()) {
            return gqlResponse.getData<ReviewReplyInsertResponse>(ReviewReplyInsertResponse::class.java).inboxReviewInsertReviewResponse
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message} )
        }
    }
}