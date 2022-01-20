package com.tokopedia.flight.cancellation.presentation.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.cancellation.data.FlightCancellationResponseEntity
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 14/07/2020
 */
@Parcelize
data class FlightCancellationModel(
    var invoiceId: String = "",
    var flightCancellationJourney: FlightCancellationResponseEntity = FlightCancellationResponseEntity(),
    var passengerModelList: MutableList<FlightCancellationPassengerModel> = arrayListOf()
) : Visitable<FlightCancellationTypeFactory>, Parcelable {

    override fun type(typeFactory: FlightCancellationTypeFactory): Int =
            typeFactory.type(this)

}