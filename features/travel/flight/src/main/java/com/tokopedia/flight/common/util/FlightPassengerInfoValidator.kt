package com.tokopedia.flight.common.util

import com.tokopedia.flight.common.util.FlightDateUtil.Companion.addTimeToSpesificDate
import com.tokopedia.flight.common.util.FlightDateUtil.Companion.removeTime
import com.tokopedia.flight.common.util.FlightDateUtil.Companion.stringToDate
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 20/03/18.
 */
class FlightPassengerInfoValidator @Inject constructor() {

    fun validateBirthdateNotEmpty(birthdate: String?): Boolean {
        return birthdate != null && birthdate.isNotEmpty()
    }

    fun validateNameIsEmpty(name: String): Boolean {
        return name.isEmpty()
    }

    fun validateNameIsNotAlphabetAndSpaceOnly(name: String): Boolean {
        return name.isNotEmpty() && !isAlphabetAndSpaceOnly(name)
    }

    fun validateFirstNameIsMoreThanMaxLength(firstName: String): Boolean {
        return firstName.length > MAX_PASSENGER_FIRST_NAME_LENGTH
    }

    fun validateLastNameIsMoreThanMaxLength(lastName: String): Boolean {
        return lastName.length > MAX_PASSENGER_LAST_NAME_LENGTH
    }

    fun validateLastNameIsLessThanMinLength(lastName: String): Boolean {
        return lastName.length < MIN_PASSENGER_LAST_NAME
    }

    fun validateLastNameIsNotSingleWord(lastName: String): Boolean {
        return lastName.isNotEmpty() && !isSingleWord(lastName)
    }

    fun validateTitleIsEmpty(title: String): Boolean {
        return title.isEmpty()
    }

    fun validateDateMoreThan(dateDefaultViewFormat: String, secondDate: Date): Boolean {
        return removeTime(stringToDate(
                FlightDateUtil.DEFAULT_VIEW_FORMAT, dateDefaultViewFormat)) > removeTime(secondDate)
    }

    fun validateDateLessThan(dateDefaultViewFormat: String, secondDate: Date): Boolean {
        return removeTime(stringToDate(
                FlightDateUtil.DEFAULT_VIEW_FORMAT, dateDefaultViewFormat)) < removeTime(secondDate)
    }

    fun validateDateNotLessThan(indicator: Date, selectedDate: String): Boolean {
        val inputDate = removeTime(stringToDate(
                FlightDateUtil.DEFAULT_VIEW_FORMAT, selectedDate))
        return inputDate.before(indicator)
    }

    fun validateExpiredDateOfPassportAtLeast6Month(expiredDateString: String, lastFlightDate: Date): Boolean {
        val expiredDate = stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, expiredDateString)
        val lastFlightDateUsed = addTimeToSpesificDate(lastFlightDate, Calendar.DATE, 0)
        return expiredDate.after(lastFlightDateUsed)
    }

    fun validateExpiredDateOfPassportMax20Years(expiredDateString: String, lastFlightDate: Date): Boolean {
        val expiredDate = stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, expiredDateString)
        return expiredDate.before(lastFlightDate)
    }

    fun validatePassportNumberNotEmpty(passportNumber: String): Boolean {
        return passportNumber.isNotEmpty()
    }

    fun validatePassportNumberAlphaNumeric(passportNumber: String): Boolean {
        return isSingleWord(passportNumber) && isAplhaNumeric(passportNumber)
    }

    fun validatePassportNumberAlphaAndNumeric(passportNumber: String): Boolean {
        return isSingleWord(passportNumber) && isAlphaAndNumeric(passportNumber)
    }

    private fun isAlphabetAndSpaceOnly(expression: String): Boolean {
        return expression.matches(REGEX_IS_ALPHABET_AND_SPACE_ONLY.toRegex())
    }

    private fun isAplhaNumeric(expression: String): Boolean {
        return expression.matches(REGEX_IS_ALPHANUMERIC_ONLY.toRegex())
    }

    private fun isAlphaAndNumeric(expression: String): Boolean {
        return expression.matches(REGEX_IS_ALPHA_AND_NUMERIC.toRegex())
    }

    private fun isSingleWord(passengerLastName: String?): Boolean {
        return passengerLastName != null && passengerLastName.split(" ".toRegex()).toTypedArray().size == 1
    }

    companion object {
        private const val MAX_PASSENGER_FIRST_NAME_LENGTH = 30
        private const val MAX_PASSENGER_LAST_NAME_LENGTH = 18
        private const val MIN_PASSENGER_LAST_NAME = 2
        private const val REGEX_IS_ALPHABET_AND_SPACE_ONLY = "^[a-zA-Z\\s]*$"
        private const val REGEX_IS_ALPHANUMERIC_ONLY = "^[a-zA-Z0-9]*$"
        private const val REGEX_IS_ALPHA_AND_NUMERIC = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$"
    }
}