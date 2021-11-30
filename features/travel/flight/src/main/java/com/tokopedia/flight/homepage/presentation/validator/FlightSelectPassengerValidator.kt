package com.tokopedia.flight.homepage.presentation.validator

import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import javax.inject.Inject

/**
 * Created by alvarisi on 10/26/17.
 */
class FlightSelectPassengerValidator @Inject constructor() {

    fun validateTotalPassenger(passData: FlightPassengerModel): Boolean {
        val total = passData.adult + passData.children
        return total <= MAX_PASSENGER_VALUE
    }

    fun validateInfantNotGreaterThanAdult(passengerPassData: FlightPassengerModel): Boolean {
        return passengerPassData.infant <= passengerPassData.adult
    }

    fun validateAdultCountAtleastOne(passengerPassData: FlightPassengerModel): Boolean {
        return passengerPassData.adult > 0
    }

    fun validateTotalPassenger(adult: Int, children: Int): Boolean {
        val total = adult + children
        return total <= MAX_PASSENGER_VALUE
    }

    fun validateInfantMoreThanFour(infant: Int): Boolean {
        return infant <= MAX_INFANT_VALUE
    }

    fun validateInfantNotGreaterThanAdult(adult: Int, infant: Int): Boolean {
        return infant <= adult
    }

    companion object {
        const val MAX_PASSENGER_VALUE = 7
        const val MAX_INFANT_VALUE = 4
    }
}