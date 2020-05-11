package com.tokopedia.reviewseller.feature.reviewreply.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.reviewseller.common.util.GQL_UPDATE_SELLER_RESPONSE
import com.tokopedia.reviewseller.feature.reviewreply.data.ReviewReplyUpdateResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class UpdateSellerResponseUseCase @Inject constructor(
        @Named(GQL_UPDATE_SELLER_RESPONSE)
        val gqlQuery: String,
        private val graphQlRepository: GraphqlRepository
): UseCase<ReviewReplyUpdateResponse.ProductrevUpdateSellerResponse>() {

    companion object {
        private const val FEEDBACK_ID = "feedbackID"
        private const val RESPONSE_MESSAGE = "responseMessage"

        @JvmStatic
        fun createParams(productId: Int, responseMessage: String): Map<String, Any> =
                mapOf(FEEDBACK_ID to productId, RESPONSE_MESSAGE to responseMessage)
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ReviewReplyUpdateResponse.ProductrevUpdateSellerResponse {
        val gqlRequest = GraphqlRequest(gqlQuery, ReviewReplyUpdateResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error.isNullOrEmpty()) {
            return gqlResponse.getData<ReviewReplyUpdateResponse>(ReviewReplyUpdateResponse::class.java).productrevUpdateSellerResponse
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message} )
        }
    }
}