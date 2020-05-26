package com.tokopedia.reviewseller.feature.reviewreply.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.reviewseller.common.util.GQL_INSERT_TEMPLATE_REVIEW
import com.tokopedia.reviewseller.feature.reviewreply.data.ReviewReplyInsertTemplateResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class InsertTemplateReviewReplyUseCase @Inject constructor(
        @Named(GQL_INSERT_TEMPLATE_REVIEW)
        val gqlQuery: String,
        private val graphQlRepository: GraphqlRepository
): UseCase<ReviewReplyInsertTemplateResponse.InsertResponseTemplate>() {

    companion object {
        private const val SHOP_ID = "shopID"
        private const val TITLE = "title"
        private const val MESSAGE = "message"

        @JvmStatic
        fun createParams(shopID: Int, title: String, message: String, responseMessage: String): Map<String, Any> =
                mapOf(SHOP_ID to shopID, TITLE to title, MESSAGE to message)
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ReviewReplyInsertTemplateResponse.InsertResponseTemplate {
        val gqlRequest = GraphqlRequest(gqlQuery, ReviewReplyInsertTemplateResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error.isNullOrEmpty()) {
            return gqlResponse.getData<ReviewReplyInsertTemplateResponse>(ReviewReplyInsertTemplateResponse::class.java).insertResponseTemplate
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message} )
        }
    }
}