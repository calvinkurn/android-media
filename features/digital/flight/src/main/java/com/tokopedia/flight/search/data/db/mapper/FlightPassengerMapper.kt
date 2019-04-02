package com.tokopedia.flight.search.data.db.mapper

import com.tokopedia.flight.passenger.data.cloud.entity.PassengerListEntity
import com.tokopedia.flight.passenger.data.db.FlightPassengerTable
import javax.inject.Inject

/**
 * Created by Rizky on 25/10/18.
 */

class FlightPassengerMapper @Inject constructor() {

    fun mapListEntityToTable(passengerListEntity: List<PassengerListEntity>) : List<FlightPassengerTable> {
        return passengerListEntity.map {
            return@map mapToFlightPassengerDb(it)
        }
    }

    fun mapToFlightPassengerDb(passengerListEntity: PassengerListEntity): FlightPassengerTable {
        val birthdate = if (passengerListEntity.passengerAttribute.dob != null) {
            passengerListEntity.passengerAttribute.dob
        } else ""

        val passportNo = if (passengerListEntity.passengerAttribute.passportNo != null) {
            passengerListEntity.passengerAttribute.passportNo
        } else ""

        val passportExpiry = if (passengerListEntity.passengerAttribute.passportExpiry != null) {
            passengerListEntity.passengerAttribute.passportExpiry
        } else ""

        val passportCountry = if (passengerListEntity.passengerAttribute.passportCountry != null) {
            passengerListEntity.passengerAttribute.passportCountry
        } else ""

        val passportNationality = if (passengerListEntity.passengerAttribute.nationality != null) {
            passengerListEntity.passengerAttribute.nationality
        } else ""

        return FlightPassengerTable(
                passengerListEntity.id,
                passengerListEntity.passengerAttribute.firstName,
                passengerListEntity.passengerAttribute.lastName,
                birthdate,
                passengerListEntity.passengerAttribute.title,
                0,
                passportNationality,
                passportCountry,
                passportExpiry,
                passportNo)
    }

}