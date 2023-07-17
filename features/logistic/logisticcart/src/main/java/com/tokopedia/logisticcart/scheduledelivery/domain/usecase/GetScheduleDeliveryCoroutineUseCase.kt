package com.tokopedia.logisticcart.scheduledelivery.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.entity.request.OngkirGetScheduledDeliveryRatesInput
import com.tokopedia.logisticcart.scheduledelivery.domain.entity.request.ScheduleDeliveryParam
import com.tokopedia.logisticcart.scheduledelivery.domain.entity.response.ScheduleDeliveryRatesResponse
import com.tokopedia.logisticcart.shipping.usecase.scheduleDeliveryRatesQuery
import javax.inject.Inject

class GetScheduleDeliveryCoroutineUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ScheduleDeliveryParam, ScheduleDeliveryRatesResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return scheduleDeliveryRatesQuery()
    }

    override suspend fun execute(params: ScheduleDeliveryParam): ScheduleDeliveryRatesResponse {
        return graphqlRepository.request(
            graphqlQuery(),
            OngkirGetScheduledDeliveryRatesInput(params)
        )
    }
}
