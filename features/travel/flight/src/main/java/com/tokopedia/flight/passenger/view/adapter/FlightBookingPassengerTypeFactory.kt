package com.tokopedia.flight.passenger.view.adapter

import com.tokopedia.flight.passenger.view.model.FlightBookingPassengerModel

/**
 * Created by alvarisi on 12/7/17.
 */
interface FlightBookingPassengerTypeFactory {

    fun type(viewModel: FlightBookingPassengerModel): Int

}