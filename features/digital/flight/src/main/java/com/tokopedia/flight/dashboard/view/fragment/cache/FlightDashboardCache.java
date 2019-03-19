package com.tokopedia.flight.dashboard.view.fragment.cache;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author by alvarisi on 11/15/17.
 */

public class FlightDashboardCache {
    private static final String CACHE_NAME = "FlightDashboardNewCache";
    private static final String DEPARTURE = "DEPARTURE_AIRPORT_ID";
    private static final String ARRIVAL = "ARRIVAL_AIRPORT_ID";
    private static final String DEPARTURE_CITY_CODE = "DEPARTURE_CITY_CODE";
    private static final String ARRIVAL_CITY_CODE = "ARRIVAL_CITY_CODE";
    private static final String DEPARTURE_CITY_NAME = "DEPARTURE_CITY_NAME";
    private static final String ARRIVAL_CITY_NAME = "ARRIVAL_CITY_NAME";
    private static final String DEPARTURE_CITY_ID = "DEPARTURE_CITY_ID";
    private static final String ARRIVAL_CITY_ID = "ARRIVAL_CITY_ID";
    private static final String DEPARTURE_DATE = "DEPARTURE_DATE";
    private static final String RETURN_DATE = "RETURN_DATE";
    private static final String PASSENGER_ADULT = "PASSENGER_ADULT";
    private static final String PASSENGER_CHILD = "PASSENGER_CHILD";
    private static final String PASSENGER_INFANT = "PASSENGER_INFANT";
    private static final String IS_ROUND_TRIP = "IS_ROUND_TRIP";
    private static final String CLASS ="CLASS";
    private static final String DEFAULT_DEPARTURE_AIRPORT_ID = "CGK";
    private static final String DEFAULT_ARRIVAL_AIRPORT_ID = "DPS";
    private static final String DEFAULT_DEPARTURE_CITY_CODE = "JKTA";
    private static final String DEFAULT_ARRIVAL_CITY_CODE = "";
    private static final String DEFAULT_DEPARTURE_CITY_NAME = "Jakarta";
    private static final String DEFAULT_ARRIVAL_CITY_NAME = "Denpasar";
    private static final String DEFAULT_EMPTY_VALUE = "";
    private static final int DEFAULT_PASSENGER_ADULT = 1;
    private static final int DEFAULT_PASSENGER_CHILD = 0;
    private static final int DEFAULT_PASSENGER_INFANT = 0;
    private static final int DEFAULT_CLASS = 1;
    private static final boolean DEFAULT_IS_ROUND_TRIP = false;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPrefs;

    public FlightDashboardCache(Context context) {
        this.sharedPrefs = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPrefs.edit();
    }

    public void putDepartureAirport(String airportId) {
        editor
                .putString(DEPARTURE, airportId)
                .apply();
    }

    public String getDepartureAirport() {
        return sharedPrefs.getString(DEPARTURE, DEFAULT_DEPARTURE_AIRPORT_ID);
    }

    public void putArrivalAirport(String airportId) {
        editor
                .putString(ARRIVAL, airportId)
                .apply();
    }

    public String getArrivalAirport() {
        return sharedPrefs.getString(ARRIVAL, DEFAULT_ARRIVAL_AIRPORT_ID);
    }

    public void putDepartureCityCode(String cityCode) {
        editor
                .putString(DEPARTURE_CITY_CODE, cityCode)
                .apply();
    }

    public String getDepartureCityCode() {
        return sharedPrefs.getString(DEPARTURE_CITY_CODE, DEFAULT_DEPARTURE_CITY_CODE);
    }

    public void putArrivalCityCode(String cityCode) {
        editor
                .putString(ARRIVAL_CITY_CODE, cityCode)
                .apply();
    }

    public String getArrivalCityCode() {
        return sharedPrefs.getString(ARRIVAL_CITY_CODE, DEFAULT_ARRIVAL_CITY_CODE);
    }

    public void putDepartureCityName(String cityName) {
        editor
                .putString(DEPARTURE_CITY_NAME, cityName)
                .apply();
    }

    public String getDepartureCityName() {
        return sharedPrefs.getString(DEPARTURE_CITY_NAME, DEFAULT_DEPARTURE_CITY_NAME);
    }

    public void putArrivalCityName(String cityName) {
        editor
                .putString(ARRIVAL_CITY_NAME, cityName)
                .apply();
    }

    public String getArrivalCityName() {
        return sharedPrefs.getString(ARRIVAL_CITY_NAME, DEFAULT_ARRIVAL_CITY_NAME);
    }

    public void putDepartureDate(String departureDate) {
        editor
                .putString(DEPARTURE_DATE, departureDate)
                .apply();
    }

    public String getDepartureDate() {
        return sharedPrefs.getString(DEPARTURE_DATE, DEFAULT_EMPTY_VALUE);
    }

    public void putReturnDate(String returnDate) {
        editor
                .putString(RETURN_DATE, returnDate)
                .apply();
    }

    public String getReturnDate() {
        return sharedPrefs.getString(RETURN_DATE, DEFAULT_EMPTY_VALUE);
    }

    public void putPassengerCount(int adult, int child, int infant) {
        editor
                .putInt(PASSENGER_ADULT, adult)
                .putInt(PASSENGER_CHILD, child)
                .putInt(PASSENGER_INFANT, infant)
                .apply();
    }

    public int getPassengerAdult() {
        return sharedPrefs.getInt(PASSENGER_ADULT, DEFAULT_PASSENGER_ADULT);
    }

    public int getPassengerChild() {
        return sharedPrefs.getInt(PASSENGER_CHILD, DEFAULT_PASSENGER_CHILD);
    }

    public int getPassengerInfant() {
        return sharedPrefs.getInt(PASSENGER_INFANT, DEFAULT_PASSENGER_INFANT);
    }

    public void putClassCache(int classId) {
        editor
                .putInt(CLASS, classId)
                .apply();
    }

    public int getClassCache() {
        return sharedPrefs.getInt(CLASS, DEFAULT_CLASS);
    }

    public void putRoundTrip(boolean isRoundTrip) {
        editor
                .putBoolean(IS_ROUND_TRIP, isRoundTrip)
                .apply();
    }

    public boolean isRoundTrip() {
        return sharedPrefs.getBoolean(IS_ROUND_TRIP, DEFAULT_IS_ROUND_TRIP);
    }

    public boolean clearCache(){
        return sharedPrefs.edit().clear().commit();
    }

}
