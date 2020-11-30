package com.tokopedia.flight.orderdetail.presentation.model.mapper

import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney
import javax.inject.Inject

/**
 * @author by furqan on 13/11/2020
 */
class FlightOrderDetailCancellationMapper @Inject constructor() {

    fun transform(orderDetailJourneyList: List<FlightOrderDetailJourneyModel>): List<FlightCancellationJourney> {
        val cancellationJourneyList = arrayListOf<FlightCancellationJourney>()

        for (journey in orderDetailJourneyList) {
            val cancellationJourney = FlightCancellationJourney()
            cancellationJourney.journeyId = journey.id.toString()
            cancellationJourney.departureTime = journey.departureTime
            cancellationJourney.departureCity = journey.departureCityName
            cancellationJourney.departureCityCode = journey.departureId
            cancellationJourney.departureAirportId = journey.departureAirportName
            cancellationJourney.arrivalTime = journey.arrivalTime
            cancellationJourney.arrivalCity = journey.arrivalCityName
            cancellationJourney.arrivalCityCode = journey.arrivalId
            cancellationJourney.arrivalAirportId = journey.arrivalAirportName

            val airlineIds = arrayListOf<String>()

            for (route in journey.routes) {
                if (cancellationJourney.airlineName.isNotEmpty() && cancellationJourney.airlineName == route.airlineName) {
                    if (route.refundable)
                        cancellationJourney.isRefundable = route.refundable
                } else if (cancellationJourney.airlineName.isNotEmpty() && cancellationJourney.airlineName != route.airlineName) {
                    cancellationJourney.airlineName = MULTI_MASKAPAI_LABEL
                    if (route.refundable)
                        cancellationJourney.isRefundable = route.refundable
                } else {
                    cancellationJourney.airlineName = route.airlineName
                    cancellationJourney.isRefundable = route.refundable
                }
                airlineIds.add(route.airlineId)
            }
            cancellationJourney.airlineIds = airlineIds

            cancellationJourneyList.add(cancellationJourney)
        }

        return cancellationJourneyList
    }

    companion object {
        const val MULTI_MASKAPAI_LABEL = "Multi Maskapai"
    }
}