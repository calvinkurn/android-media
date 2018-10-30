package com.tokopedia.flight.searchV2.data.repository.util

import com.tokopedia.flight.searchV2.data.api.single.response.*
import java.util.ArrayList

/**
 * Created by Rizky on 14/10/18.
 */
fun createFlightDataResponse(journeyId: String): FlightDataResponse<List<FlightSearchData>> {
    val flightDataResponse = FlightDataResponse<List<FlightSearchData>>()
    val flightSearchDataList = ArrayList<FlightSearchData>()
    val flightSearchData = createFlightSearchData(journeyId)
    flightSearchDataList.add(flightSearchData)
    flightDataResponse.data = flightSearchDataList
    flightDataResponse.meta = Meta()
    return flightDataResponse
}

fun createFlightListSearchDataResponse(journeyId1: String, journeyId2: String): FlightDataResponse<List<FlightSearchData>> {
    val flightDataResponse = FlightDataResponse<List<FlightSearchData>>()
    val flightSearchDataList = ArrayList<FlightSearchData>()
    val flightSearchData1 = createFlightSearchData(journeyId1)
    val flightSearchData2 = createFlightSearchData(journeyId2)
    flightSearchDataList.add(flightSearchData1)
    flightSearchDataList.add(flightSearchData2)
    flightDataResponse.data = flightSearchDataList
    flightDataResponse.meta = Meta()
    return flightDataResponse
}

private fun createFlightSearchData(journeyId: String): FlightSearchData {
    val flightSearchData = FlightSearchData()
    var attributes = Attributes()
    val routes = arrayListOf<Route>()
    val route = Route("JT", "CGK", "123", "DPS",
            "234", "2j", "layover", null, "JT123",
            true, null, 1, null, "Lion Air",
            "logo", "Cengkareng", "Jakarta",
            "Denpasar", "Denpasar")
    routes.add(route)
    attributes.routes = routes
    attributes.departureAirport = "CGK"
    attributes.departureTime = "16:00"
    attributes.departureTimeInt = 123
    attributes.arrivalAirport = "DPS"
    attributes.arrivalTime = "18:00"
    attributes.arrivalTimeInt = 234
    attributes.addDayArrival = 0
    attributes.aid = ""
    attributes.duration = "2j"
    attributes.durationMinute = 12345
    attributes.beforeTotal = "Rp 550.000"
    attributes.total = "Rp 500.000"
    attributes.totalNumeric = 500000
    attributes.term = "CGKDPS"
    attributes.totalStop = 1
    attributes.totalTransit = 0
    val fare = Fare()
    fare.adult = "Rp 500.000"
    fare.adultNumeric = 500000
    fare.child = "Rp 0"
    fare.childNumeric = 0
    fare.infant = "Rp 0"
    fare.infantNumeric = 0
    attributes.fare = fare
    flightSearchData.id = journeyId
    flightSearchData.attributes = attributes
    return flightSearchData
}
