package com.tokopedia.flight.cancellationdetail.presentation.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailPassengerTypeFactory
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailAmenityModel
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 06/01/2021
 */
@Parcelize
data class FlightOrderCancellationDetailPassengerModel(
        val id: Int,
        val type: Int,
        val typeString: String,
        val title: Int,
        val titleString: String,
        val firstName: String,
        val lastName: String,
        val departureAirportId: String,
        val arrivalAirportId: String,
        val journeyId: Int,
        val amenities: List<FlightOrderDetailAmenityModel>
) : Parcelable, Visitable<FlightOrderCancellationDetailPassengerTypeFactory> {
    override fun type(typeFactory: FlightOrderCancellationDetailPassengerTypeFactory): Int =
            typeFactory.type(this)

}