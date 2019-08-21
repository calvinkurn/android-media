package com.tokopedia.common.travel.data

import com.tokopedia.common.travel.data.entity.FlightCountryEntity
import com.tokopedia.flight.country.database.CountryPhoneCodeTable
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 12/03/19.
 */
class PhoneCodeListMapper @Inject constructor() {

    fun mapEntitiesToTables(entities: List<FlightCountryEntity>) : List<CountryPhoneCodeTable> {
        return entities.map {
                mapEntityToTable(it)
        }
    }

    fun mapEntityToTable(flightCountryEntity: FlightCountryEntity) : CountryPhoneCodeTable {
        return CountryPhoneCodeTable(
                flightCountryEntity.id,
                flightCountryEntity.attributesEntity.name,
                flightCountryEntity.attributesEntity.phoneCode)
    }
}
