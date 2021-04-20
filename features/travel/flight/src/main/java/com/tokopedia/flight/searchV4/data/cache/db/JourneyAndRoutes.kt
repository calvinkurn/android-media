package com.tokopedia.flight.searchV4.data.cache.db

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Furqan on 05/11/20.
 */
class JourneyAndRoutes(
        @Embedded
        var flightJourneyTable: FlightJourneyTable = FlightJourneyTable(),
        @Relation(parentColumn = "id", entityColumn = "journeyId")
        var routes: List<FlightRouteTable> = arrayListOf())
