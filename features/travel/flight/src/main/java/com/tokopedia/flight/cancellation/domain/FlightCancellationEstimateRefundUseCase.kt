package com.tokopedia.flight.cancellation.domain

import com.tokopedia.flight.cancellation.data.FlightCancellationEstimateEntity
import com.tokopedia.flight.cancellation.data.FlightCancellationGQLQuery
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

/**
 * @author by furqan on 21/07/2020
 */
class FlightCancellationEstimateRefundUseCase @Inject constructor(
        private val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(requestParams: Map<String, Any>): FlightCancellationEstimateEntity {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        useCase.clearRequest()

        val graphqlRequest = GraphqlRequest(FlightCancellationGQLQuery.CANCEL_ESTIMATE,
                FlightCancellationEstimateEntity.Response::class.java, requestParams)
        useCase.addRequest(graphqlRequest)

        return useCase.executeOnBackground()
                .getSuccessData<FlightCancellationEstimateEntity.Response>().flightEstimated
    }

    fun createRequestParams(invoiceId: String,
                            userId: String,
                            cancellationList: List<FlightCancellationModel>,
                            reasonType: Int): Map<String, Any> =
            mapOf(
                    PARAM_DATA to mapOf(
                            PARAM_INVOICE_ID to invoiceId,
                            PARAM_USER_ID to userId.toLong(),
                            PARAM_REASON_TYPE to reasonType,
                            PARAM_DETAILS to transform(cancellationList)
                    )
            )

    private fun transform(cancellationList: List<FlightCancellationModel>)
            : List<Map<String, Any>> {
        val detailList = arrayListOf<Map<String, Any>>()

        for (cancellation in cancellationList) {
            for (passenger in cancellation.passengerModelList) {
                detailList.add(mapOf<String, Any>(
                        PARAM_JOURNEY_ID to cancellation.flightCancellationJourney.journeyId.toLong(),
                        PARAM_PASSENGER_ID to passenger.passengerId.toLong()
                ))
            }
        }

        return detailList
    }

    companion object {
        private const val PARAM_DATA = "data"
        private const val PARAM_INVOICE_ID = "invoiceID"
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_REASON_TYPE = "reasonType"
        private const val PARAM_DETAILS = "details"
        private const val PARAM_JOURNEY_ID = "journeyID"
        private const val PARAM_PASSENGER_ID = "passengerID"
    }

}