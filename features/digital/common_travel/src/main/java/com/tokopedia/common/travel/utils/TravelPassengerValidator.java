package com.tokopedia.common.travel.utils;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 04/12/18.
 */
public class TravelPassengerValidator {

    private static final String PASSENGER_NAME_REGEX = "^[a-zA-Z\\s]*$";
    private static final String PASSENGER_ID_NUMBER_REGEX = "^[A-Za-z0-9]+$";
    private static final int MAX_CONTACT_NAME = 60;
    private static final int MAX_IDENTITY_NUMBER = 20;
    private static final int MIN_IDENTITY_NUMBER = 5;

    @Inject
    public TravelPassengerValidator() {
    }

    public boolean isSalutationEmpty(String salutationTitle) {
        return salutationTitle == null || salutationTitle.length() == 0;
    }

    public boolean isNameEmpty(String name) {
        return name == null || name.length() == 0;
    }

    public boolean isNameMoreThanMax(String name) {
        return name.length() > MAX_CONTACT_NAME;
    }

    public boolean isNameContainSpace(String name) {
        return name.contains(" ");
    }

    public boolean isNameUseSpecialCharacter(String name) {
        return !name.matches(PASSENGER_NAME_REGEX);
    }

    public boolean isBirthdateEmpty(String birthdate) {
        return birthdate == null || birthdate.length() == 0;
    }

    public boolean isIdentityNumberEmpty(String idNumber) {
        return idNumber == null || idNumber.length() == 0;
    }

    public boolean isIdentityNumberLessThanMin(String idNumber) {
        return idNumber.length() < MIN_IDENTITY_NUMBER;
    }

    public boolean isIdentityNumberMoreThanMax(String idNumber) {
        return idNumber.length() > MAX_IDENTITY_NUMBER;
    }

    public boolean isIdNumberUseSpecialCharacter(String idNumber) {
        return !idNumber.matches(PASSENGER_ID_NUMBER_REGEX);
    }
}
