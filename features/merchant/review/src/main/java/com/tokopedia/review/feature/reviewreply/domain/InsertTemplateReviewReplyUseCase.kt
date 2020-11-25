package com.tokopedia.review.feature.reviewreply.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.reviewreply.data.ReviewReplyInsertTemplateResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class InsertTemplateReviewReplyUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
): UseCase<ReviewReplyInsertTemplateResponse.InsertResponseTemplate>() {

    companion object {
        private const val SHOP_ID = "shopID"
        private const val TITLE = "title"
        private const val MESSAGE = "message"
        const val INSERT_TEMPLATE_REVIEW_MUTATION_CLASS_NAME = "InsertTemplateReview"
        const val INSERT_TEMPLATE_REVIEW_MUTATION = """
            mutation insert_template_review(${'$'}shopID: Int!, ${'$'}title: String!, ${'$'}message: String!) {
              insertResponseTemplate(shopID: ${'$'}shopID, title: ${'$'}title, message: ${'$'}message) {
                    success
                    data {
                       templateId
                       title
                       message
                       status
                     }
                     defaultTemplateID
                     error
                  }
            }
        """

        @JvmStatic
        fun createParams(shopID: Int, title: String, message: String): Map<String, Any> =
                mapOf(SHOP_ID to shopID, TITLE to title, MESSAGE to message)
    }

    var params = mapOf<String, Any>()

    @GqlQuery(INSERT_TEMPLATE_REVIEW_MUTATION_CLASS_NAME, INSERT_TEMPLATE_REVIEW_MUTATION)
    override suspend fun executeOnBackground(): ReviewReplyInsertTemplateResponse.InsertResponseTemplate {
        val gqlRequest = GraphqlRequest(InsertTemplateReview.GQL_QUERY, ReviewReplyInsertTemplateResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error.isNullOrEmpty()) {
            return gqlResponse.getData<ReviewReplyInsertTemplateResponse>(ReviewReplyInsertTemplateResponse::class.java).insertResponseTemplate
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message} )
        }
    }
}