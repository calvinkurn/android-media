package com.tokopedia.flight.orderlist.domain.model.mapper

import com.tokopedia.flight.orderlist.constant.FlightCancellationStatus
import com.tokopedia.flight.orderlist.data.cloud.entity.CancellationEntity
import com.tokopedia.flight.orderlist.data.cloud.entity.PassengerAmentityEntity
import com.tokopedia.flight.orderlist.data.cloud.entity.PassengerEntity
import com.tokopedia.flight.orderlist.domain.model.FlightOrderPassengerViewModel
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderAmenityViewModel
import java.util.*
import javax.inject.Inject

/**
 * Created by alvarisi on 12/12/17.
 */

class FlightOrderPassengerViewModelMapper @Inject
constructor() {

    fun transformAmenities(amenities: List<PassengerAmentityEntity>): List<FlightOrderAmenityViewModel> {
        return amenities.map {
            val amenityViewModel = FlightOrderAmenityViewModel()
            amenityViewModel.id = it.sequence.toString()
            amenityViewModel.price = it.price
            amenityViewModel.priceNumeric = it.priceNumeric
            amenityViewModel.title = it.detail
            amenityViewModel.departureId = it.departureAirportId
            amenityViewModel.arrivalId = it.arrivalAirportId
            amenityViewModel.amenityType = it.amenityType
            return@map amenityViewModel
        }
    }

    fun transform(entities: List<PassengerEntity>): List<FlightOrderPassengerViewModel> {
        return entities.map {
            return@map FlightOrderPassengerViewModel(
                    it.type,
                    if (it.cancelStatus.isNotEmpty()) it.cancelStatus[0].status else 0,
                    if (it.cancelStatus.size > 1) it.cancelStatus[1].status else 0,
                    it.title,
                    it.firstName,
                    it.lastName,
                    transformAmenities(it.amenities),
                    if (it.cancelStatus.isNotEmpty()) it.cancelStatus[0].statusStr else "",
                    if (it.cancelStatus.size > 1) it.cancelStatus[1].statusStr else "")
        }
    }

    fun getCancelledPassengerCount(cancellations: List<CancellationEntity>): Int {
        val cancelledPassengers = ArrayList<String>()

        for (item in cancellations) {
            for (attribute in item.details) {
                if (!cancelledPassengers.contains(attribute.passengerId + attribute.journeyId) && (item.status == FlightCancellationStatus.PENDING ||
                                item.status == FlightCancellationStatus.REFUNDED ||
                                item.status == FlightCancellationStatus.REQUESTED)) {
                    cancelledPassengers.add(attribute.passengerId + attribute.journeyId)
                }
            }
        }

        return cancelledPassengers.size
    }
}
