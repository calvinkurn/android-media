package com.tokopedia.flight.homepage.data.cache

import android.content.Context
import android.content.SharedPreferences

/**
 * @author by alvarisi on 11/15/17.
 */
class FlightDashboardCache(context: Context) {
    private val editor: SharedPreferences.Editor
    private val sharedPrefs: SharedPreferences

    init {
        sharedPrefs = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE)
        editor = sharedPrefs.edit()
    }

    fun putDepartureAirport(airportId: String?) {
        editor
                .putString(DEPARTURE_AIRPORT_CODE, airportId)
                .apply()
    }

    val departureAirport: String
        get() = sharedPrefs.getString(DEPARTURE_AIRPORT_CODE, DEFAULT_DEPARTURE_AIRPORT_ID) ?: DEFAULT_DEPARTURE_AIRPORT_ID

    fun putArrivalAirport(airportId: String?) {
        editor
                .putString(ARRIVAL_AIRPORT_CODE, airportId)
                .apply()
    }

    val arrivalAirport: String
        get() = sharedPrefs.getString(ARRIVAL_AIRPORT_CODE, DEFAULT_ARRIVAL_AIRPORT_ID) ?: DEFAULT_ARRIVAL_AIRPORT_ID

    fun putDepartureCityCode(cityCode: String?) {
        editor
                .putString(DEPARTURE_CITY_CODE, cityCode)
                .apply()
    }

    val departureCityCode: String
        get() = sharedPrefs.getString(DEPARTURE_CITY_CODE, DEFAULT_DEPARTURE_CITY_CODE) ?: DEFAULT_DEPARTURE_CITY_CODE

    fun putArrivalCityCode(cityCode: String?) {
        editor
                .putString(ARRIVAL_CITY_CODE, cityCode)
                .apply()
    }

    val arrivalCityCode: String
        get() = sharedPrefs.getString(ARRIVAL_CITY_CODE, DEFAULT_ARRIVAL_CITY_CODE) ?: DEFAULT_ARRIVAL_CITY_CODE

    fun putDepartureCityName(cityName: String?) {
        editor
                .putString(DEPARTURE_CITY_NAME, cityName)
                .apply()
    }

    val departureCityName: String
        get() = sharedPrefs.getString(DEPARTURE_CITY_NAME, DEFAULT_DEPARTURE_CITY_NAME) ?: DEFAULT_DEPARTURE_CITY_NAME

    fun putArrivalCityName(cityName: String?) {
        editor
                .putString(ARRIVAL_CITY_NAME, cityName)
                .apply()
    }

    val arrivalCityName: String
        get() = sharedPrefs.getString(ARRIVAL_CITY_NAME, DEFAULT_ARRIVAL_CITY_NAME) ?: DEFAULT_ARRIVAL_CITY_NAME

    fun putDepartureDate(departureDate: String?) {
        editor
                .putString(DEPARTURE_DATE, departureDate)
                .apply()
    }

    val departureDate: String
        get() = sharedPrefs.getString(DEPARTURE_DATE, DEFAULT_EMPTY_VALUE) ?: DEFAULT_EMPTY_VALUE

    fun putReturnDate(returnDate: String?) {
        editor
                .putString(RETURN_DATE, returnDate)
                .apply()
    }

    val returnDate: String
        get() = sharedPrefs.getString(RETURN_DATE, DEFAULT_EMPTY_VALUE) ?: DEFAULT_EMPTY_VALUE

    fun putPassengerCount(adult: Int, child: Int, infant: Int) {
        editor
                .putInt(PASSENGER_ADULT, adult)
                .putInt(PASSENGER_CHILD, child)
                .putInt(PASSENGER_INFANT, infant)
                .apply()
    }

    val passengerAdult: Int
        get() = sharedPrefs.getInt(PASSENGER_ADULT, DEFAULT_PASSENGER_ADULT)

    val passengerChild: Int
        get() = sharedPrefs.getInt(PASSENGER_CHILD, DEFAULT_PASSENGER_CHILD)

    val passengerInfant: Int
        get() = sharedPrefs.getInt(PASSENGER_INFANT, DEFAULT_PASSENGER_INFANT)

    fun putClassCache(classId: Int) {
        editor
                .putInt(CLASS, classId)
                .apply()
    }

    val classCache: Int
        get() = sharedPrefs.getInt(CLASS, DEFAULT_CLASS)

    fun putRoundTrip(isRoundTrip: Boolean) {
        editor
                .putBoolean(IS_ROUND_TRIP, isRoundTrip)
                .apply()
    }

    val isRoundTrip: Boolean
        get() = sharedPrefs.getBoolean(IS_ROUND_TRIP, DEFAULT_IS_ROUND_TRIP)

    fun clearCache(): Boolean {
        return sharedPrefs.edit().clear().commit()
    }

    companion object {
        private const val CACHE_NAME = "FlightDashboardNewCache"
        private const val DEPARTURE_CITY_CODE = "DEPARTURE_CITY_CODE"
        private const val ARRIVAL_CITY_CODE = "ARRIVAL_CITY_CODE"
        private const val DEPARTURE_CITY_NAME = "DEPARTURE_CITY_NAME"
        private const val ARRIVAL_CITY_NAME = "ARRIVAL_CITY_NAME"
        private const val DEPARTURE_AIRPORT_CODE = "DEPARTURE_AIRPORT_CODE"
        private const val ARRIVAL_AIRPORT_CODE = "ARRIVAL_AIRPORT_CODE"
        private const val DEPARTURE_DATE = "DEPARTURE_DATE"
        private const val RETURN_DATE = "RETURN_DATE"
        private const val PASSENGER_ADULT = "PASSENGER_ADULT"
        private const val PASSENGER_CHILD = "PASSENGER_CHILD"
        private const val PASSENGER_INFANT = "PASSENGER_INFANT"
        private const val IS_ROUND_TRIP = "IS_ROUND_TRIP"
        private const val CLASS = "CLASS"
        private const val DEFAULT_DEPARTURE_AIRPORT_ID = "CGK"
        private const val DEFAULT_ARRIVAL_AIRPORT_ID = "DPS"
        private const val DEFAULT_DEPARTURE_CITY_CODE = "JKTA"
        private const val DEFAULT_ARRIVAL_CITY_CODE = ""
        private const val DEFAULT_DEPARTURE_CITY_NAME = "Jakarta"
        private const val DEFAULT_ARRIVAL_CITY_NAME = "Denpasar"
        private const val DEFAULT_EMPTY_VALUE = ""
        private const val DEFAULT_PASSENGER_ADULT = 1
        private const val DEFAULT_PASSENGER_CHILD = 0
        private const val DEFAULT_PASSENGER_INFANT = 0
        private const val DEFAULT_CLASS = 1
        private const val DEFAULT_IS_ROUND_TRIP = false
    }
}