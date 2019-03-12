package com.tokopedia.flight.airport.data.source

import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry
import com.tokopedia.flight.airport.data.source.database.FlightAirportCountryTable
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 12/03/19.
 */
class FlightAirportMapper @Inject constructor() {

    fun mapEntitiesToTables(entities: List<FlightAirportCountry>) : List<FlightAirportCountryTable> {
        return entities.map {
                mapEntityToTable(it)
        }
    }

    fun mapEntityToTable(flightAirportCountry: FlightAirportCountry) : FlightAirportCountryTable {
        return FlightAirportCountryTable(
                flightAirportCountry.id,
                flightAirportCountry.attributes.name,
                flightAirportCountry.attributes.phoneCode)
    }
}
