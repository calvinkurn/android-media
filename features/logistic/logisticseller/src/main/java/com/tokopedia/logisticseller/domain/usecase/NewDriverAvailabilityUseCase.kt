package com.tokopedia.logisticseller.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticseller.data.param.NewDriverParam
import com.tokopedia.logisticseller.data.response.NewDriverAvailabilityResponse
import javax.inject.Inject

class NewDriverAvailabilityUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<NewDriverParam, NewDriverAvailabilityResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return NEW_DRIVER_AVAILABILITY
    }

    override suspend fun execute(params: NewDriverParam): NewDriverAvailabilityResponse {
        return repository.request(
            graphqlQuery(),
            params
        )
    }

    companion object {
        const val NEW_DRIVER_AVAILABILITY = """
            query mpLogisticNewDriverAvailability(${'$'}order_id:String!){
	            mpLogisticNewDriverAvailability(orderID: ${'$'}order_id) {
                    message
                    awbnum
                    order_id
                    order_tx_id
                    available_time
                    invoice
                    availability_retry
	            }
            }
        """
    }
}
