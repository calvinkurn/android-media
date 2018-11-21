package com.tokopedia.flight.search.data.db.util

import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum
import com.tokopedia.flight.search.data.db.FlightComboTable
import com.tokopedia.flight.search.data.db.FlightJourneyTable
import com.tokopedia.flight.search.data.db.FlightRouteTable

/**
 * Created by Rizky on 07/10/18.
 */
fun createCombo(onwardJourneyId: String, returnJourneyId: String): FlightComboTable {
    return FlightComboTable(
            onwardJourneyId,
            "Rp 250.000", "Rp 0", "Rp 0",
            250000, 0,0,
            returnJourneyId,
            "Rp 250.000", "Rp 0", "Rp 0",
            250000, 0, 0,
            "comboId", true
    )
}

fun createFlightJourneyTable(journeyId: String): FlightJourneyTable {
    return FlightJourneyTable(
            journeyId,
            "CGKDPS",
            "CGK",
            "Cengkareng",
            "jakarta",
            "DPS",
            "Denpasar",
            "",
            null,
            "16.00",
            123,
            "18.00",
            234,
            0,
            0,
            0,
            "2j",
            0,
            "Rp 500.000",
            "",
            500000,
            0,
            "Rp 200.000",
            "",
            200000,
            0,
            "Rp 0",
            "",
            0,
            0,
            "Rp 500000",
            "",
            500000,
            0,
            false,
            "Rp 600000",
            "Rp 500000",
            500000,
            false,
            RefundableEnum.REFUNDABLE,
            false,
            "comboId"
    )
}

fun createRoutes(journeyId: String): List<FlightRouteTable> {
    val routes = arrayListOf<FlightRouteTable>()
    routes.add(FlightRouteTable(journeyId, "JT", "Lion Air", "shortName", "Lion Air Logo",
            "CGK", "Cengkareng", "Jakarta", "DPS",
            "Denpasar", "Bali", "123", "234",
            "2j", "infos", "layover", "ABCDE", true,
            "amenities", 1, "stopDetail"))
    return routes
}

