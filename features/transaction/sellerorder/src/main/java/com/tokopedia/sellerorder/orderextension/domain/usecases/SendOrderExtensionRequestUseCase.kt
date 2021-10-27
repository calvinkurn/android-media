package com.tokopedia.sellerorder.orderextension.domain.usecases

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.orderextension.domain.models.SendOrderExtensionRequestParam
import com.tokopedia.sellerorder.orderextension.domain.models.SendOrderExtensionRequestResponse
import javax.inject.Inject

class SendOrderExtensionRequestUseCase @Inject constructor(
    private val graphQlRepository: GraphqlRepository
) {
    suspend fun execute(
        userId: String,
        orderId: String,
        shopId: String,
        reasonCode: Int,
        reasonText: String
    ): SendOrderExtensionRequestResponse.Data.OrderExtensionRequest {
        val requests = createRequests(userId, orderId, shopId, reasonCode, reasonText)
        val responses = graphQlRepository.response(requests)
        return responses.getSuccessData<SendOrderExtensionRequestResponse.Data>().orderExtensionRequest
    }

    private fun createRequests(
        userId: String,
        orderId: String,
        shopId: String,
        reasonCode: Int,
        reasonText: String
    ): List<GraphqlRequest> {
        return listOf(
            GraphqlRequest(
                QUERY,
                SendOrderExtensionRequestResponse.Data::class.java,
                generateRequestParams(userId, orderId, shopId, reasonCode, reasonText)
            )
        )
    }

    private fun generateRequestParams(
        userId: String,
        orderId: String,
        shopId: String,
        reasonCode: Int,
        reasonText: String
    ): Map<String, Any> {
        return mapOf(
            "input" to SendOrderExtensionRequestParam(
                userId,
                orderId,
                shopId,
                reasonCode,
                reasonText
            )
        )
    }

    companion object {
        private val QUERY = """
            mutation SomOrderExtensionRequest(${'$'}input:OrderExtensionRequestRequest!) {
              order_extension_info(input: ${'$'}input) {
                message
                message_code
              }
            }
        """.trimIndent()
    }
}