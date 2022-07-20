package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.mapper.BuyerOrderExtensionMapper
import com.tokopedia.buyerorderdetail.domain.models.OrderExtensionRespondInfoResponse
import com.tokopedia.buyerorderdetail.domain.models.OrderExtensionRespondResponse
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondUiModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.io.IOException
import javax.inject.Inject

class InsertOrderExtensionRespondUseCase @Inject constructor(
    private val mapper: BuyerOrderExtensionMapper,
    private val graphqlRepository: GraphqlRepository
) : UseCase<OrderExtensionRespondUiModel>() {

    private var params = RequestParams.create()

    override suspend fun executeOnBackground(): OrderExtensionRespondUiModel {
        val actionType = params.getInt(ACTION_KEY, EXTENSION_ACTION)
        val orderExtensionRespondRequest = GraphqlRequest(
            QUERY,
            OrderExtensionRespondResponse::class.java,
            params.parameters
        )
        val gqlResponse = graphqlRepository.response(listOf(orderExtensionRespondRequest))
        try {
            val orderExtensionRespondResponse =
                gqlResponse.getData<OrderExtensionRespondResponse>(
                    OrderExtensionRespondResponse::class.java
                )
            return mapper.mapToOrderExtensionRespond(
                orderExtensionRespondResponse.data,
                actionType
            )
        } catch (e: IOException) {
            throw IOException(e.message)
        } catch (e: Exception) {
            val error = gqlResponse.getError(OrderExtensionRespondInfoResponse::class.java)
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun setParams(
        orderId: Long,
        action: Int,
    ) {
        params = RequestParams.create().apply {
            putLong(ORDER_ID_KEY, orderId)
            putInt(ACTION_KEY, action)
        }
    }

    companion object {
        private const val ORDER_ID_KEY = "order_id"
        private const val ACTION_KEY = "action"
        private const val EXTENSION_ACTION = 1

        private val QUERY = """
            mutation OrderExtensionRespond(${'$'}order_id: Int!, ${'$'}action: Int!) {
              order_extension_respond(input: {
                order_id: ${'$'}order_id
                action: ${'$'}action
              }) {
                message
                message_code
              }
            }
        """.trimIndent()
    }
}