package com.tokopedia.logisticorder.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticorder.usecase.entity.RetryBookingResponse
import com.tokopedia.logisticorder.usecase.query.TrackingPageQuery
import javax.inject.Inject

class SetRetryBookingUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, RetryBookingResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return TrackingPageQuery.retryBooking
    }

    override suspend fun execute(orderId: String): RetryBookingResponse {
        val param = mapOf("id" to orderId)
        return gql.request(graphqlQuery(), param)
    }
}
