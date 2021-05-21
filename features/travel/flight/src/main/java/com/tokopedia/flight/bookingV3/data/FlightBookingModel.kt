package com.tokopedia.flight.bookingV3.data

import android.os.Parcelable
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import kotlinx.android.parcel.Parcelize

/**
 * @author by jessica on 2019-11-05
 */
@Parcelize
data class FlightBookingModel (
        var departureId: String = "",
        var returnId: String = "",
        var departureDate: String = "",
        var departureTerm: String = "",
        var returnTerm: String = "",
        var cartId: String = "",
        var isDomestic: Boolean = true,
        var isMandatoryDob: Boolean = false,
        var flightPriceModel: FlightPriceModel = FlightPriceModel(),
        var searchParam: FlightSearchPassDataModel = FlightSearchPassDataModel(),
        var insurances: List<FlightCart.Insurance> = arrayListOf()
): Parcelable