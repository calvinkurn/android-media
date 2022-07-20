package com.tokopedia.sellerorder.orderextension.domain.usecases

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.orderextension.domain.models.GetOrderExtensionRequestInfoParam
import com.tokopedia.sellerorder.orderextension.domain.models.GetOrderExtensionRequestInfoResponse
import javax.inject.Inject

class GetOrderExtensionRequestInfoUseCase @Inject constructor(
    private val graphQlRepository: GraphqlRepository
) {
    suspend fun execute(
        orderId: String,
        shopId: String,
    ): GetOrderExtensionRequestInfoResponse.Data.OrderExtensionRequestInfo.OrderExtensionRequestInfoData {
        val requests = createRequests(orderId, shopId)
        val responses = graphQlRepository.response(requests)
        return responses.getSuccessData<GetOrderExtensionRequestInfoResponse.Data>().orderExtensionRequestInfo.data
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

    private fun generateRequestParams(
        orderId: String,
        shopId: String
    ): Map<String, Any> {
        return mapOf(
            PARAM_INPUT to GetOrderExtensionRequestInfoParam(
                orderId.toLongOrZero(),
                shopId.toLongOrZero()
            )
        )
    }

    companion object {
        private val QUERY = """
            query SomOrderExtensionRequestInfo(${'$'}input: OrderExtensionRequestInfoRequest!) {
              order_extension_request_info(input: ${'$'}input) {
                data {
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
            }
        """.trimIndent()

        private const val PARAM_INPUT = "input"
    }
}