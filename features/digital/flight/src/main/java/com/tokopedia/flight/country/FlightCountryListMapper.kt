package com.tokopedia.flight.country

import com.tokopedia.flight.country.data.FlightCountryEntity
import com.tokopedia.flight.country.database.FlightAirportCountryTable
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 12/03/19.
 */
class FlightCountryListMapper @Inject constructor() {

    fun mapEntitiesToTables(entities: List<FlightCountryEntity>) : List<FlightAirportCountryTable> {
        return entities.map {
                mapEntityToTable(it)
        }
    }

    fun mapEntityToTable(flightCountryEntity: FlightCountryEntity) : FlightAirportCountryTable {
        return FlightAirportCountryTable(
                flightCountryEntity.id,
                flightCountryEntity.attributesEntity.name,
                flightCountryEntity.attributesEntity.phoneCode)
    }
}
