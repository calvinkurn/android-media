package com.tokopedia.reviewseller.feature.reviewreply.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.reviewseller.common.util.GQL_GET_TEMPLATE_LIST
import com.tokopedia.reviewseller.feature.reviewreply.data.ReviewReplyTemplateListResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetReviewTemplateListUseCase @Inject constructor(
        @Named(GQL_GET_TEMPLATE_LIST)
        val gqlQuery: String,
        private val graphQlRepository: GraphqlRepository
): UseCase<ReviewReplyTemplateListResponse.ReviewResponseTemplateList>() {

    companion object {
        private const val SHOP_ID = "shopId"

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