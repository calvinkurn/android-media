package com.tokopedia.review.feature.reviewreply.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.reviewreply.data.ReviewReplyTemplateListResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetReviewTemplateListUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
): UseCase<ReviewReplyTemplateListResponse.ReviewResponseTemplateList>() {

    companion object {
        private const val SHOP_ID = "shopId"
        private val gqlQuery = """
            query get_review_response_template_list(${'$'}shopId: Int!) {
                reviewResponseTemplateList(shopId: ${'$'}shopId) {
                  autoReply {
                    autoReplyId
                    status
                    templateId
                  }
                  list {
                    templateId
                    title
                    message
                    status
                  }
                }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(shopId: Int): Map<String, Any> = mapOf(SHOP_ID to shopId)
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ReviewReplyTemplateListResponse.ReviewResponseTemplateList {
        val gqlRequest = GraphqlRequest(gqlQuery, ReviewReplyTemplateListResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error.isNullOrEmpty()) {
            return gqlResponse.getData<ReviewReplyTemplateListResponse>(ReviewReplyTemplateListResponse::class.java).reviewResponseTemplateList
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message} )
        }
    }
}