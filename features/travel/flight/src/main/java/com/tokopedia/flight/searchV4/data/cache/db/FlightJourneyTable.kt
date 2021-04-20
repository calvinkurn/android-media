package com.tokopedia.flight.searchV4.data.cache.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.tokopedia.flight.searchV4.presentation.model.FlightAirlineModel
import com.tokopedia.flight.searchV4.presentation.model.filter.RefundableEnum

/**
 * Created by Furqan on 05/11/20.
 */
@Entity
class FlightJourneyTable(
        @PrimaryKey
        var id: String = "",
        var term: String = "",
        var hasFreeRapidTest: Boolean = false,
        var isSeatDistancing: Boolean = false,
        var departureAirport: String = "",
        var departureAirportName: String = "",
        var departureAirportCity: String = "",
        var arrivalAirport: String = "",
        var arrivalAirportName: String = "",
        var arrivalAirportCity: String = "",
        @Ignore
        var flightAirlineDBS: List<FlightAirlineModel>? = null,
        var departureTime: String = "",
        var departureTimeInt: Int = 0,
        var arrivalTime: String = "",
        var arrivalTimeInt: Int = 0,
        var totalTransit: Int = 0,
        var totalStop: Int = 0,
        var addDayArrival: Int = 0,
        var duration: String = "",
        var durationMinute: Int = 0,
        var adult: String = "",
        var adultCombo: String = "",
        var adultNumeric: Int = 0,
        var adultNumericCombo: Int = 0,
        var child: String = "",
        var childCombo: String = "",
        var childNumeric: Int = 0,
        var childNumericCombo: Int = 0,
        var infant: String = "",
        var infantCombo: String = "",
        var infantNumeric: Int = 0,
        var infantNumericCombo: Int = 0,
        var total: String = "",
        var totalCombo: String = "",
        var totalNumeric: Int = 0,
        var totalNumericCombo: Int = 0,
        var isBestPairing: Boolean = false,
        var beforeTotal: String = "",
        var isShowSpecialPriceTag: Boolean = false,
        var sortPrice: String = "",
        var sortPriceNumeric: Int = 0,
        var isReturn: Boolean = false,
        var isSpecialPrice: Boolean = false,
        var isRefundable: RefundableEnum = RefundableEnum.NOT_REFUNDABLE,
        var comboId: String = "") {

    fun setIsRefundable(isRefundable: RefundableEnum) {
        this.isRefundable = isRefundable
    }

}