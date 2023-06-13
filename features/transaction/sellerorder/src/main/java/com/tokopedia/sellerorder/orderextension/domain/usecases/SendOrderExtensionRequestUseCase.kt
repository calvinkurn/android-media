package com.tokopedia.sellerorder.orderextension.domain.usecases

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.orderextension.domain.models.SendOrderExtensionRequestParam
import com.tokopedia.sellerorder.orderextension.domain.models.SendOrderExtensionRequestResponse
import javax.inject.Inject

class SendOrderExtensionRequestUseCase @Inject constructor(
    private val graphQlRepository: GraphqlRepository
) {
    suspend fun execute(
        orderId: String,
        shopId: String,
        reasonCode: Int,
        reasonText: String,
        extensionTime: Int
    ): SendOrderExtensionRequestResponse.Data.OrderExtensionRequest.OrderExtensionRequestData {
        val requests = createRequests(orderId, shopId, reasonCode, reasonText, extensionTime)
        val responses = graphQlRepository.response(requests)
        return responses.getSuccessData<SendOrderExtensionRequestResponse.Data>().orderExtensionRequest.data
    }

    private fun createRequests(
        orderId: String,
        shopId: String,
        reasonCode: Int,
        reasonText: String,
        extensionTime: Int
    ): List<GraphqlRequest> {
        return listOf(
            GraphqlRequest(
                QUERY,
                SendOrderExtensionRequestResponse.Data::class.java,
                generateRequestParams(orderId, shopId, reasonCode, reasonText, extensionTime)
            )
        )
    }

    private fun generateRequestParams(
        orderId: String,
        shopId: String,
        reasonCode: Int,
        reasonText: String,
        extensionTime: Int
    ): Map<String, Any> {
        return mapOf(
            PARAM_INPUT to SendOrderExtensionRequestParam(
                orderId.toLongOrZero(),
                shopId.toLongOrZero(),
                reasonCode,
                reasonText,
                extensionTime
            )
        )
    }

    companion object {
        private val QUERY = """
            mutation SomOrderExtensionRequest(${'$'}input:OrderExtensionRequestRequest!) {
              order_extension_request(input: ${'$'}input) {
                data {
                  message
                  message_code
                }
              }
            }
        """.trimIndent()

        private const val PARAM_INPUT = "input"
    }
}
