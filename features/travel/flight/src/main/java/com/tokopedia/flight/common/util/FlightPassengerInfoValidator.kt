package com.tokopedia.flight.common.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import com.tokopedia.utils.date.removeTime
import com.tokopedia.utils.date.toDate
import java.text.ParseException
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
        try {
            return dateDefaultViewFormat.toDate(DateUtil.DEFAULT_VIEW_FORMAT)
                .removeTime() > secondDate.removeTime()
        } catch (parseException: ParseException) {
            FirebaseCrashlytics.getInstance().recordException(parseException)
            return false
        }
    }

    fun validateDateLessThan(dateDefaultViewFormat: String, secondDate: Date): Boolean {
        return dateDefaultViewFormat.toDate(DateUtil.DEFAULT_VIEW_FORMAT).removeTime() < secondDate.removeTime()
    }

    fun validateDateNotLessThan(indicator: Date, selectedDate: String): Boolean {
        val inputDate = selectedDate.toDate(DateUtil.DEFAULT_VIEW_FORMAT).removeTime()
        return inputDate.before(indicator)
    }

    fun validateExpiredDateOfPassportAtLeast6Month(expiredDateString: String, lastFlightDate: Date): Boolean {
        val expiredDate = expiredDateString.toDate(DateUtil.DEFAULT_VIEW_FORMAT)
        val lastFlightDateUsed = lastFlightDate.addTimeToSpesificDate(Calendar.DATE, 0)
        return expiredDate.after(lastFlightDateUsed)
    }

    fun validateExpiredDateOfPassportMax20Years(expiredDateString: String, lastFlightDate: Date): Boolean {
        val expiredDate = expiredDateString.toDate(DateUtil.DEFAULT_VIEW_FORMAT)
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

    fun isNumberOnly(number: String): Boolean{
        return number.matches(REGEX_IS_NUMBER_ONLY.toRegex())
    }

    fun validateIdenNumLength(number: String): Boolean {
        return (number.length < IDENTIFICATION_NUMBER_LENGTH || number.length > IDENTIFICATION_NUMBER_LENGTH)
    }

    companion object {
        const val MIN_PASSPORT_NUMBER = 6
        const val MAX_PASSPORT_NUMBER = 10

        private const val MAX_PASSENGER_FIRST_NAME_LENGTH = 30
        private const val MAX_PASSENGER_LAST_NAME_LENGTH = 18
        private const val MIN_PASSENGER_LAST_NAME = 2
        private const val IDENTIFICATION_NUMBER_LENGTH = 16
        private const val REGEX_IS_ALPHABET_AND_SPACE_ONLY = "^[a-zA-Z\\s]*$"
        private const val REGEX_IS_NUMBER_ONLY = "^[0-9]"
        private const val REGEX_IS_ALPHANUMERIC_ONLY = "^[a-zA-Z0-9]*$"
        private const val REGEX_IS_ALPHA_AND_NUMERIC = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$"
    }
}
