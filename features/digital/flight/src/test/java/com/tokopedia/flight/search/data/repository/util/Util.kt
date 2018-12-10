package com.tokopedia.flight.search.data.repository.util

import com.tokopedia.flight.search.data.api.combined.response.AttributesResponse
import com.tokopedia.flight.search.data.api.combined.response.ComboResponse
import com.tokopedia.flight.search.data.api.combined.response.FlightSearchCombinedResponse
import com.tokopedia.flight.search.data.api.single.response.*
import java.util.*

/**
 * Created by Rizky on 14/10/18.
 */
fun createFlightDataResponse(journeyId: String): FlightDataResponse<List<FlightSearchData>> {
    val flightSearchData = createFlightSearchData(journeyId)
    val flightSearchDataList = ArrayList<FlightSearchData>()
    flightSearchDataList.add(flightSearchData)
    val included1 = Included("airline", "JT",
            AttributesAirline("Lion Air", "Lion", "logo", ""))
    val included2 = Included("airport", "CGK",
            AttributesAirport("Cengkareng", "Jakarta"))
    val included3 = Included("airport", "DPS",
            AttributesAirport("Denpasar", "Denpasar"))
    val flightDataResponse = FlightDataResponse<List<FlightSearchData>>()
    flightDataResponse.data = flightSearchDataList
    flightDataResponse.meta = Meta()
    flightDataResponse.included = listOf(included1, included2, included3)
    return flightDataResponse
}

fun createFlightListSearchDataResponse(journeyId1: String, journeyId2: String): FlightDataResponse<List<FlightSearchData>> {
    val flightSearchData1 = createFlightSearchData(journeyId1)
    val flightSearchData2 = createFlightSearchData(journeyId2)
    val flightSearchDataList = ArrayList<FlightSearchData>()
    flightSearchDataList.add(flightSearchData1)
    flightSearchDataList.add(flightSearchData2)
    val included1 = Included("airline", "JT",
            AttributesAirline("Lion Air", "Lion", "logo", ""))
    val included2 = Included("airport", "CGK",
            AttributesAirport("Cengkareng", "Jakarta"))
    val included3 = Included("airport", "DPS",
            AttributesAirport("Denpasar", "Denpasar"))
    val flightDataResponse = FlightDataResponse<List<FlightSearchData>>()
    flightDataResponse.data = flightSearchDataList
    flightDataResponse.meta = Meta()
    flightDataResponse.included = listOf(included1, included2, included3)
    return flightDataResponse
}

private fun createFlightSearchData(journeyId: String): FlightSearchData {
    val route = Route("JT", "CGK", "123", "DPS",
            "234", "2j", "layover", null, "JT123",
            true, null, 1, null, "Lion Air",
            "logo", "Cengkareng", "Jakarta",
            "Denpasar", "Denpasar")
    val routes = arrayListOf<Route>()
    routes.add(route)

    val fare = Fare("Rp 500.000", "Rp 0", "Rp 0", 500000, 0, 0)

    val attributes = Attributes("CGKDPS", "", "CGK", "16:00",
            123, "DPS", "18:00", 234, routes, 0,
            1, 0, "2j", 12345, "Rp 550.000",
            500000, "Rp 550.000", fare)

    return FlightSearchData("journey", journeyId, attributes)
}

fun createCombine(): FlightDataResponse<List<FlightSearchCombinedResponse>> {
    val combos = arrayListOf<ComboResponse>()
    val comboResponse1 = ComboResponse("1", "Rp 500.000", "Rp. 250.000", "Rp 0",
            500000, 250000, 0)
    combos.add(comboResponse1)

    val comboResponse2 = ComboResponse("2", "Rp 500.000", "Rp. 250.000", "Rp 0",
            500000, 250000, 0)
    combos.add(comboResponse2)

    val attributesResponse = AttributesResponse(combos, true)

    val flightSearchCombined = FlightSearchCombinedResponse("combined", "combined_id", attributesResponse)

    val flightSearchCombinedList = arrayListOf<FlightSearchCombinedResponse>()
    flightSearchCombinedList.add(flightSearchCombined)

    val flightDataResponse = FlightDataResponse<List<FlightSearchCombinedResponse>>()
    flightDataResponse.data = flightSearchCombinedList
    flightDataResponse.meta = Meta()

    return flightDataResponse
}
