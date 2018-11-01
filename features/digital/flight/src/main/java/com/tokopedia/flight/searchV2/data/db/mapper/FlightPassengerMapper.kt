package com.tokopedia.flight.searchV2.data.db.mapper

import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.passenger.data.cloud.entity.PassengerListEntity
import com.tokopedia.flight_dbflow.FlightPassengerDB

/**
 * Created by Rizky on 25/10/18.
 */
class FlightPassengerMapper {

    fun mapToFlightPassengerDb(passengerListEntity: PassengerListEntity) : FlightPassengerDB {
        var birthdate = if (passengerListEntity.passengerAttribute.dob != null) {
            FlightDateUtil.formatDate(
                    FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    FlightDateUtil.DEFAULT_FORMAT,
                    passengerListEntity.passengerAttribute.dob
            )
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

        return FlightPassengerDB(
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