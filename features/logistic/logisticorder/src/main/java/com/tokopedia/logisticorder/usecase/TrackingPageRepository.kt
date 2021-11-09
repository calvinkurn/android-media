package com.tokopedia.logisticorder.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logisticorder.domain.response.GetDriverTipResponse
import com.tokopedia.logisticorder.domain.response.GetLogisticTrackingResponse
import com.tokopedia.logisticorder.usecase.entity.RetryAvailabilityResponse
import com.tokopedia.logisticorder.usecase.entity.RetryBookingResponse
import com.tokopedia.logisticorder.usecase.query.TrackingPageQuery
import com.tokopedia.logisticorder.utils.getResponse
import javax.inject.Inject

class TrackingPageRepository @Inject constructor(@ApplicationContext private val gql: GraphqlRepository){

    suspend fun getTrackingPage(orderId: String, from: String) : GetLogisticTrackingResponse {
        val param = mapOf("input" to mapOf(
                "order_id" to orderId,
                "from" to from))
        val request = GraphqlRequest(TrackingPageQuery.getTrackingPage,
                GetLogisticTrackingResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun retryBooking(orderId: String) : RetryBookingResponse {
        val param = mapOf("id" to orderId)
        val request = GraphqlRequest(TrackingPageQuery.retryBooking,
                RetryBookingResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun retryAvailability(orderId: String) : RetryAvailabilityResponse {
        val param = mapOf("id" to orderId)
        val request = GraphqlRequest(TrackingPageQuery.retryAvailability,
                RetryAvailabilityResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun getDriverTip(orderId: String?) : GetDriverTipResponse {
        val param = mapOf("input" to mapOf(
            "order_id" to orderId))
        val request = GraphqlRequest(TrackingPageQuery.getDriverTip,
            GetDriverTipResponse::class.java, param)
        return gql.getResponse(request)
    }


}