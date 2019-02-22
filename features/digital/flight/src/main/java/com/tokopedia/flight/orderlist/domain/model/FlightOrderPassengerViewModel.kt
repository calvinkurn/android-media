package com.tokopedia.flight.orderlist.domain.model

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel

/**
 * Created by alvarisi on 12/12/17.
 */

class FlightOrderPassengerViewModel(
        val type: Int,
        val status: Int,
        val passengerTitleId: Int,
        val passengerFirstName: String,
        val passengerLastName: String,
        val passengerBirthdate: String,
        val amenities: List<FlightBookingAmenityViewModel>)
