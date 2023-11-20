package com.tokopedia.logisticorder.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticorder.domain.response.GetLogisticTrackingResponse
import com.tokopedia.logisticorder.usecase.params.GetTrackingParam
import com.tokopedia.logisticorder.usecase.query.TrackingPageQuery
import javax.inject.Inject

class GetTrackingUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetTrackingParam, GetLogisticTrackingResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return TrackingPageQuery.getTrackingPage
    }

    override suspend fun execute(getTrackingParam: GetTrackingParam): GetLogisticTrackingResponse {
        return gql.request(graphqlQuery(), getTrackingParam)
    }

    fun getParam(
        orderId: String,
        orderTxId: String?,
        groupType: Int?,
        from: String
    ): GetTrackingParam {
        return GetTrackingParam(
            GetTrackingParam.TrackingParam(
                orderId = orderId,
                orderTxId = orderTxId ?: "",
                groupType = groupType ?: 0,
                from = from
            )
        )
    }
}
