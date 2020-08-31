package com.tokopedia.flight.homepage.presentation.validator;

import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel;

import javax.inject.Inject;

/**
 * Created by alvarisi on 10/26/17.
 */

public class FlightSelectPassengerValidator {
    public static final int MAX_PASSENGER_VALUE = 7;
    public static final int MAX_INFANT_VALUE = 4;

    @Inject
    public FlightSelectPassengerValidator() {
    }

    public boolean validateTotalPassenger(FlightPassengerModel passData) {
        int total = passData.getAdult() + passData.getChildren();
        return total <= MAX_PASSENGER_VALUE;
    }

    public boolean validateInfantNotGreaterThanAdult(FlightPassengerModel passengerPassData) {
        return passengerPassData.getInfant() <= passengerPassData.getAdult();
    }

    public boolean validateAdultCountAtleastOne(FlightPassengerModel passengerPassData) {
        return passengerPassData.getAdult() > 0;
    }

    public boolean validateTotalPassenger(int adult, int children) {
        int total = adult + children;
        return total <= MAX_PASSENGER_VALUE;
    }

    public boolean validateInfantMoreThanFour(int infant) {
        return infant <= MAX_INFANT_VALUE;
    }

    public boolean validateInfantNotGreaterThanAdult(int adult, int infant) {
        return infant <= adult;
    }

    public boolean validateAdultCountAtleastOne(int adult) {
        return adult > 0;
    }
}
