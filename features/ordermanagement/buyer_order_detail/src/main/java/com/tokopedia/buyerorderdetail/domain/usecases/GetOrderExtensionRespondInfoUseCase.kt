package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.mapper.BuyerOrderExtensionMapper
import com.tokopedia.buyerorderdetail.domain.models.OrderExtensionRespondInfoResponse
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondInfoUiModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.io.IOException
import javax.inject.Inject

class GetOrderExtensionRespondInfoUseCase @Inject constructor(
    private val mapper: BuyerOrderExtensionMapper,
    private val graphqlRepository: GraphqlRepository
) : UseCase<OrderExtensionRespondInfoUiModel>() {

    private var params = RequestParams.create()

    override suspend fun executeOnBackground(): OrderExtensionRespondInfoUiModel {
        val orderId = params.getLong(ORDER_ID_KEY, ORDER_ID_DEFAULT)
        val orderExtensionRespondInfoRequest = GraphqlRequest(
            QUERY,
            OrderExtensionRespondInfoResponse::class.java,
            params.parameters
        )
        val gqlResponse = graphqlRepository.response(listOf(orderExtensionRespondInfoRequest))
        try {
            val orderExtensionRespondInfoResponse =
                gqlResponse.getData<OrderExtensionRespondInfoResponse>(
                    OrderExtensionRespondInfoResponse::class.java
                )
            return mapper.mapToOrderExtensionRespondInfo(
                orderExtensionRespondInfoResponse.data,
                orderId.toString()
            )
        } catch (e: IOException) {
            throw IOException(e.message)
        } catch (e: Exception) {
            val error = gqlResponse.getError(OrderExtensionRespondInfoResponse::class.java)
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun setParams(
        orderId: Long
    ) {
        params = RequestParams.create().apply {
            putLong(ORDER_ID_KEY, orderId)
        }
    }

    companion object {
        private const val ORDER_ID_KEY = "order_id"
        private const val ORDER_ID_DEFAULT = 0L

        private val QUERY = """
            query OrderExtensionRespondInfo(${'$'}order_id: Int!) {
              order_extension_respond_info(input: {
                order_id : ${'$'}order_id
              }) {
                text
                reject_text
                new_deadline
                reason
                message_code
                message
              }
            }
        """.trimIndent()
    }
}