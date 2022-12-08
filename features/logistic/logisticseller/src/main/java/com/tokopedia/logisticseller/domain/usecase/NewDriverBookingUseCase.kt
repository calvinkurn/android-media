package com.tokopedia.logisticseller.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticseller.data.param.NewDriverParam
import com.tokopedia.logisticseller.data.response.NewDriverBookingResponse
import javax.inject.Inject

class NewDriverBookingUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<NewDriverParam, NewDriverBookingResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return NEW_DRIVER_BOOKING
    }

    override suspend fun execute(params: NewDriverParam): NewDriverBookingResponse {
        return repository.request(
            graphqlQuery(),
            params
        )
    }

    companion object {
        const val NEW_DRIVER_BOOKING = """
            mutation MpLogisticNewDriverBooking(${'$'}order_id: String!){
	            MpLogisticNewDriverBooking(orderID: ${'$'}order_id) {
                    message
                    order_id
                    order_tx_id
                    awbnum
                    shipper_id
                    shipper_product_id
                    invoice
	            }
            }
        """
    }
}
