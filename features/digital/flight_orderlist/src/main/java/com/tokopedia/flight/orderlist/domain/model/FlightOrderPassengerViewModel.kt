package com.tokopedia.flight.orderlist.domain.model

import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderAmenityViewModel

/**
 * Created by alvarisi on 12/12/17.
 */

class FlightOrderPassengerViewModel(
        val type: Int,
        val status: Int,
        val secondStatus: Int,
        val passengerTitleId: Int,
        val passengerFirstName: String,
        val passengerLastName: String,
        val amenities: List<FlightOrderAmenityViewModel>,
        val cancellationStatusStr: String,
        val secondCancellationStatusStr: String)
