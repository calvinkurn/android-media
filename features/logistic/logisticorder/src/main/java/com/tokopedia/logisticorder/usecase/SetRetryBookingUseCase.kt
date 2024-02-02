package com.tokopedia.logisticorder.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticorder.usecase.entity.RetryBookingResponse
import javax.inject.Inject

class SetRetryBookingUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, RetryBookingResponse>(dispatcher.io) {

    override suspend fun execute(orderId: String): RetryBookingResponse {
        val param = mapOf("id" to orderId)
        return gql.request(graphqlQuery(), param)
    }

    override fun graphqlQuery() = """
        mutation RetryBooking(${'$'}id: String!){
          retryBooking(orderID: ${'$'}id){
            order_id
            order_tx_id
            awbnum
            shipper_id
            shipper_product_id
          }
        }
    """.trimIndent()
}
