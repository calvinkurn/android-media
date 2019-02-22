package com.tokopedia.flight.orderlist.domain.model.mapper

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel
import com.tokopedia.flight.cancellation.constant.FlightCancellationStatus
import com.tokopedia.flight.orderlist.data.cloud.entity.CancellationDetailsAttribute
import com.tokopedia.flight.orderlist.data.cloud.entity.CancellationEntity
import com.tokopedia.flight.orderlist.data.cloud.entity.PassengerAmentityEntity
import com.tokopedia.flight.orderlist.data.cloud.entity.PassengerEntity
import com.tokopedia.flight.orderlist.domain.model.FlightOrderPassengerViewModel
import java.util.*
import javax.inject.Inject

/**
 * Created by alvarisi on 12/12/17.
 */

class FlightOrderPassengerViewModelMapper @Inject
constructor() {

    fun transformAmenities(amenities: List<PassengerAmentityEntity>): List<FlightBookingAmenityViewModel> {
        return amenities.map {
            val amenityViewModel = FlightBookingAmenityViewModel()
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

    fun transform(entities: List<PassengerEntity>, cancellations: List<CancellationEntity>): List<FlightOrderPassengerViewModel> {
        return entities.map {
            return@map FlightOrderPassengerViewModel(
                    it.type,
                    getPassengerStatus(it.id, cancellations),
                    it.title,
                    it.firstName,
                    it.lastName,
                    it.dob,
                    transformAmenities(it.amenities))
        }
    }

    private fun getPassengerStatus(passengerId: String, cancellations: List<CancellationEntity>): Int {
        var status = 0
        val passenger = CancellationDetailsAttribute(passengerId = passengerId)
        cancellations.map {
            if (it.details.contains(passenger)) {
                if (it.status == FlightCancellationStatus.PENDING ||
                        it.status == FlightCancellationStatus.REFUNDED ||
                        it.status == FlightCancellationStatus.REQUESTED) {
                    status = it.status
                    return@map
                } else if (it.status == FlightCancellationStatus.ABORTED) {
                    status = it.status
                }
            }
        }
        return status
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
