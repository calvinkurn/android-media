package com.tokopedia.logisticorder.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticorder.domain.response.GetDriverTipResponse
import com.tokopedia.logisticorder.usecase.query.TrackingPageQuery
import javax.inject.Inject

class GetDriverTipUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String?, GetDriverTipResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return TrackingPageQuery.getDriverTip
    }

    override suspend fun execute(orderId: String?): GetDriverTipResponse {
        val param = mapOf(
            "input" to mapOf(
                "order_id" to orderId
            )
        )
        return gql.request(graphqlQuery(), param)
    }
}
