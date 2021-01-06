package com.tokopedia.flight.cancellationdetail.presentation.model

import android.os.Parcelable
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailAmenityModel
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 06/01/2021
 */
@Parcelize
data class FlightOrderCancellationDetailPassengerModel(
        val journeyId: Long,
        val departureAirportId: String,
        val arrivalAirportId: String,
        val amenitites: List<FlightOrderDetailAmenityModel>
) : Parcelable