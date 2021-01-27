package com.tokopedia.flight.searchV4.presentation.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.searchV4.data.cloud.single.Route
import com.tokopedia.flight.searchV4.presentation.adapter.viewholder.FlightSearchAdapterTypeFactory
import com.tokopedia.flight.searchV4.presentation.model.filter.RefundableEnum
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 25/06/2020
 */
@Parcelize
data class FlightJourneyModel(val term: String,
                              val id: String,
                              val hasFreeRapidTest: Boolean,
                              val isSeatDistancing: Boolean,
                              val departureAirport: String,
                              val departureAirportName: String,
                              val departureAirportCity: String,
                              val departureTime: String,
                              val departureTimeInt: Int,
                              val arrivalAirport: String,
                              val arrivalTime: String,
                              val arrivalAirportName: String,
                              val arrivalAirportCity: String,
                              val arrivalTimeInt: Int,
                              val totalTransit: Int,
                              val addDayArrival: Int,
                              val duration: String,
                              val durationMinute: Int,
                              val total: String,
                              val totalNumeric: Int,
                              val comboPrice: String,
                              val comboPriceNumeric: Int,
                              val isBestPairing: Boolean,
                              val beforeTotal: String,
                              val showSpecialPriceTag: Boolean,
                              val isRefundable: RefundableEnum,
                              val isReturning: Boolean,
                              val fare: FlightFareModel,
                              val routeList: List<Route>,
                              val airlineDataList: List<FlightAirlineModel>,
                              val comboId: String,
                              val specialTagText: String)
    : Visitable<FlightSearchAdapterTypeFactory>,
        Parcelable {

    override fun type(typeFactory: FlightSearchAdapterTypeFactory): Int =
            typeFactory.type(this)

}