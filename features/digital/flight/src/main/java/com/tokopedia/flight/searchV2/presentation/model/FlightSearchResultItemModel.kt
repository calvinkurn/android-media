package com.tokopedia.flight.searchV2.presentation.model

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB
import com.tokopedia.flight.search.data.cloud.model.response.Fare
import com.tokopedia.flight.search.data.cloud.model.response.Route
import com.tokopedia.flight.search.view.model.filter.RefundableEnum

/**
 * Created by Rizky on 20/09/18.
 */
class FlightSearchResultItemModel(
         val term: String,
         val id: String?,
         val type: String?,
         val departureAirport: String?,
         val departureAirportName: String?,
         val departureAirportCity: String?,
         val departureTime: String?, //ini waktu berangkat 2018-01-01T14:45:00Z
         val departureTimeInt: Int, //1450
         val arrivalAirport: String?,
         val arrivalTime: String?,
         val arrivalAirportName: String?, // merge result
         val arrivalAirportCity: String?, // merge result
         val arrivalTimeInt: Int, //1450
         val totalTransit: Int,
         val addDayArrival: Int,
         val duration: String?, // 1 jam 50 menit
         val durationMinute: Int,
         val total: String?, // 693000
         val totalNumeric: Int, // Fare "Rp 693.000"
         val beforeTotal: String?,
//    private val isRefundable: RefundableEnum = null
         val isReturning: Boolean,
         val fare: Fare?,
         val routeList: List<Route>?,
         val airlineDataList: List<FlightAirlineDB>? // merge result
)