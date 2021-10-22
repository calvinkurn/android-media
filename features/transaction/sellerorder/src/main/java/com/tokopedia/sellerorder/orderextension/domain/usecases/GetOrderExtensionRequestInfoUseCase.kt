package com.tokopedia.sellerorder.orderextension.domain.usecases

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.orderextension.domain.models.GetOrderExtensionRequestInfoParam
import com.tokopedia.sellerorder.orderextension.domain.models.GetOrderExtensionRequestInfoResponse
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetOrderExtensionRequestInfoUseCase @Inject constructor(
    private val graphQlRepository: GraphqlRepository
) {
    suspend fun execute(
        orderId: String,
        shopId: String
    ): GetOrderExtensionRequestInfoResponse.Data.OrderExtensionRequestInfo {
        val requests = createRequests(orderId, shopId)
        val responses = graphQlRepository.response(requests)
        delay((Math.random() * 2000).toLong())
        return responses.getSuccessData<GetOrderExtensionRequestInfoResponse.Data>().orderExtensionRequestInfo
    }

    private fun createRequests(orderId: String, shopId: String): List<GraphqlRequest> {
        return listOf(
            GraphqlRequest(
                QUERY,
                GetOrderExtensionRequestInfoResponse.Data::class.java,
                generateRequestParams(orderId, shopId)
            )
        )
    }

    private fun generateRequestParams(orderId: String, shopId: String): Map<String, Any> {
        return mapOf("input" to GetOrderExtensionRequestInfoParam(orderId, shopId))
    }

    companion object {
        private val QUERY = """
            query SomOrderExtensionRequestInfo(${'$'}input: OrderExtensionRequestInfoRequest!) {
              order_extension_request_info(input: ${'$'}input) {
                text
                new_deadline
                reason {
                  reason_title
                  reason_code
                  must_comment
                }
                message
                message_code
              }
            }
        """.trimIndent()
    }
}