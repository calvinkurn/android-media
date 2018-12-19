package com.tokopedia.common.travel.utils;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 04/12/18.
 */
public class TravelPassengerValidator {

    private static final String REGEX_NAME = "^[a-zA-Z\\s]*$";
    private static final String REGEX_ID_NUMBER = "^[A-Za-z0-9]+$";
    private static final String REGEX_NUMERIC = "^[0-9\\s]*$";
    private static final String REGEX_EMAIL = "^[A-Za-z0-9_.-]+@(.+)$";
    private static final int MAX_CONTACT_NAME = 60;
    private static final int MAX_IDENTITY_NUMBER = 20;
    private static final int MIN_IDENTITY_NUMBER = 5;
    private static final int MIN_PHONE_NUMBER = 9;
    private static final int MAX_PHONE_NUMBER = 15;

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

    public boolean isNameMoreThanTwoWords(String name) {
        String[] nameList = name.split(" ");
        return nameList.length > 1;
    }

    public boolean isNameUseSpecialCharacter(String name) {
        return !name.matches(REGEX_NAME);
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
        return !idNumber.matches(REGEX_ID_NUMBER);
    }

    public boolean isPhoneNumberEmpty(String phoneNumber) {
        return phoneNumber == null || phoneNumber.length() == 0;
    }

    public boolean isPhoneNumberUseChar(String phoneNumber) {
        return !phoneNumber.matches(REGEX_NUMERIC);
    }

    public boolean isPhoneNumberLessThanMin(String phoneNumber) {
        return phoneNumber.length() < MIN_PHONE_NUMBER;
    }

    public boolean isPhoneNumberMoreThanMax(String phoneNumber) {
        return phoneNumber.length() > MAX_PHONE_NUMBER;
    }

    public boolean isEmailEmpty(String email) {
        return email == null || email.length() == 0;
    }

    public boolean isEmailNotValid(String email) {
        return !email.matches(REGEX_EMAIL);
    }
}
