package com.tokopedia.train.homepage.presentation;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.homepage.presentation.model.TrainHomepageViewModel;
import com.tokopedia.train.homepage.presentation.model.TrainPassengerViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationAndCityViewModel;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rizky on 10/07/18.
 */
public class TrainHomepageCache {

    private static final String CACHE_NAME = "TrainHomepageCache";

    private final int DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL = 2;

    private final String DEFAULT_ORIGIN_STATION_CODE = "GMR";
    private final String DEFAULT_ORIGIN_CITY_NAME = "Jakarta";
    private final String DEFAULT_ORIGIN_ISLAND_NAME = "Jawa";
    private final String DEFAULT_DESTINATION_STATION_CODE = "BD";
    private final String DEFAULT_DESTINATION_CITY_NAME = "Bandung";
    private final String DEFAULT_DESTINATION_ISLAND_NAME = "Jawa";
    private final int DEFAULT_NUM_OF_ADULT_PASSENGER = 1;
    private final int DEFAULT_NUM_OF_INFANT_PASSENGER = 0;
    private final boolean DEFAULT_IS_ONE_WAY = true;

    private static final String ORIGIN_STATION_CODE = "ORIGIN_STATION_CODE";
    private static final String ORIGIN_CITY_NAME = "ORIGIN_CITY_NAME";
    private static final String ORIGIN_ISLAND_NAME = "ORIGIN_ISLAND_NAME";
    private static final String DESTINATION_STATION_CODE = "DESTINATION_STATION_CODE";
    private static final String DESTINATION_CITY_NAME = "DESTINATION_CITY_NAME";
    private static final String DESTINATION_ISLAND_NAME = "DESTINATION_ISLAND_NAME";
    private static final String DEPARTURE_DATE = "DEPARTURE_DATE";
    private static final String DEPARTURE_DATE_FMT = "DEPARTURE_DATE_FMT";
    private static final String RETURN_DATE = "RETURN_DATE";
    private static final String RETURN_DATE_FMT = "RETURN_DATE_FMT";
    private static final String NUM_OF_ADULT_PASSENGER = "NUM_OF_ADULT_PASSENGER";
    private static final String NUM_OF_INFANT_PASSENGER = "NUM_OF_INFANT_PASSENGER";
    private static final String PASSENGER_FMT = "PASSENGER_FMT";
    private static final String IS_ONE_WAY = "IS_ONE_WAY";

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPrefs;

    private Context context;

    public TrainHomepageCache(Context context) {
        this.context = context;
        this.sharedPrefs = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPrefs.edit();
    }

    public void saveToCache(TrainHomepageViewModel trainHomepageViewModel) {
        if (trainHomepageViewModel != null) {
            editor.putString(ORIGIN_STATION_CODE, trainHomepageViewModel.getOriginStation()
                    .getStationCode()).apply();
            editor.putString(ORIGIN_CITY_NAME, trainHomepageViewModel.getOriginStation()
                    .getCityName()).apply();
            editor.putString(ORIGIN_ISLAND_NAME, trainHomepageViewModel.getOriginStation()
                    .getIslandName()).apply();

            editor.putString(DESTINATION_STATION_CODE, trainHomepageViewModel.getDestinationStation()
                    .getStationCode()).apply();
            editor.putString(DESTINATION_CITY_NAME, trainHomepageViewModel.getDestinationStation()
                    .getCityName()).apply();
            editor.putString(DESTINATION_ISLAND_NAME, trainHomepageViewModel.getDestinationStation()
                    .getIslandName()).apply();

            boolean isOneWay = trainHomepageViewModel.isOneWay();
            editor.putBoolean(IS_ONE_WAY, isOneWay).apply();
            editor.putString(DEPARTURE_DATE, trainHomepageViewModel.getDepartureDate()).apply();
            editor.putString(DEPARTURE_DATE_FMT, trainHomepageViewModel.getDepartureDateFmt().toString()).apply();
            if (!isOneWay) {
                editor.putString(RETURN_DATE, trainHomepageViewModel.getReturnDate()).apply();
                editor.putString(RETURN_DATE_FMT, trainHomepageViewModel.getReturnDateFmt().toString()).apply();
            }

            editor.putInt(NUM_OF_ADULT_PASSENGER, trainHomepageViewModel.getTrainPassengerViewModel().getAdult());
            editor.putInt(NUM_OF_INFANT_PASSENGER, trainHomepageViewModel.getTrainPassengerViewModel().getInfant());
            editor.putString(PASSENGER_FMT, trainHomepageViewModel.getPassengerFmt()).apply();
        }
    }

    public TrainHomepageViewModel buildTrainHomepageViewModelFromCache() {
        String originStationCode = sharedPrefs.getString(ORIGIN_STATION_CODE, DEFAULT_ORIGIN_STATION_CODE);
        String originCityName = sharedPrefs.getString(ORIGIN_CITY_NAME, DEFAULT_ORIGIN_CITY_NAME);
        String originIslandName = sharedPrefs.getString(ORIGIN_ISLAND_NAME, DEFAULT_ORIGIN_ISLAND_NAME);
        TrainStationAndCityViewModel originTrainStationAndCityViewModel;
        if (!TextUtils.isEmpty(originStationCode)) {
            originTrainStationAndCityViewModel = new TrainStationAndCityViewModel(
                    originStationCode, originCityName, originIslandName
            );
        } else {
            if (!TextUtils.isEmpty(originCityName)) {
                originTrainStationAndCityViewModel = new TrainStationAndCityViewModel(
                        "", originCityName, originIslandName
                );
            } else {
                originTrainStationAndCityViewModel = new TrainStationAndCityViewModel(
                        ORIGIN_STATION_CODE, originCityName, originIslandName
                );
            }
        }

        String destinationStationCode = sharedPrefs.getString(DESTINATION_STATION_CODE, DEFAULT_DESTINATION_STATION_CODE);
        String destinationCityName = sharedPrefs.getString(DESTINATION_CITY_NAME, DEFAULT_DESTINATION_CITY_NAME);
        String destinationIslandName = sharedPrefs.getString(DESTINATION_ISLAND_NAME, DEFAULT_DESTINATION_ISLAND_NAME);
        TrainStationAndCityViewModel destinationTrainStationAndCityViewModel;
        if (!TextUtils.isEmpty(destinationStationCode)) {
            destinationTrainStationAndCityViewModel = new TrainStationAndCityViewModel(
                    destinationStationCode, destinationCityName, destinationIslandName
            );
        } else {
            if (!TextUtils.isEmpty(destinationCityName)) {
                destinationTrainStationAndCityViewModel = new TrainStationAndCityViewModel(
                        "", destinationCityName, destinationIslandName
                );
            } else {
                destinationTrainStationAndCityViewModel = new TrainStationAndCityViewModel(
                        DEFAULT_DESTINATION_STATION_CODE, destinationCityName, destinationIslandName
                );
            }
        }

        Date departureDateCal = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 1); // departure date = today + 1
        String departureDate = sharedPrefs.getString(DEPARTURE_DATE,
                TrainDateUtil.dateToString(departureDateCal, TrainDateUtil.DEFAULT_FORMAT));
        String departureDateFmt = sharedPrefs.getString(DEPARTURE_DATE_FMT,
                TrainDateUtil.dateToString(departureDateCal, TrainDateUtil.DEFAULT_VIEW_FORMAT));
        if (TrainDateUtil.stringToDate(departureDate).before(TrainDateUtil.getCurrentDate())) {
            departureDate = TrainDateUtil.dateToString(departureDateCal, TrainDateUtil.DEFAULT_FORMAT);
            departureDateFmt = TrainDateUtil.dateToString(departureDateCal, TrainDateUtil.DEFAULT_VIEW_FORMAT);
        }

        boolean isOneWay = sharedPrefs.getBoolean(IS_ONE_WAY, DEFAULT_IS_ONE_WAY);

        Date returnDateCal = TrainDateUtil.addTimeToSpesificDate(departureDateCal, Calendar.DATE, DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL); // return date = departure date + 2
        String returnDate = sharedPrefs.getString(RETURN_DATE, TrainDateUtil.dateToString(returnDateCal, TrainDateUtil.DEFAULT_FORMAT));
        String returnDateFmt = sharedPrefs.getString(RETURN_DATE_FMT, TrainDateUtil.dateToString(returnDateCal, TrainDateUtil.DEFAULT_VIEW_FORMAT));

        int numOfAdultPassenger = sharedPrefs.getInt(NUM_OF_ADULT_PASSENGER, DEFAULT_NUM_OF_ADULT_PASSENGER);
        int numOfInfantPassenger = sharedPrefs.getInt(NUM_OF_INFANT_PASSENGER, DEFAULT_NUM_OF_INFANT_PASSENGER);
        TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel(
                numOfAdultPassenger, numOfInfantPassenger);

        String defaultPassengerFmt = buildPassengerTextFormatted(trainPassengerViewModel);
        String passengerFmt = sharedPrefs.getString(PASSENGER_FMT, defaultPassengerFmt);

        return new TrainHomepageViewModel.Builder()
                .setOriginStation(originTrainStationAndCityViewModel)
                .setDestinationStation(destinationTrainStationAndCityViewModel)
                .setDepartureDate(departureDate)
                .setDepartureDateFmt(departureDateFmt)
                .setReturnDate(returnDate)
                .setReturnDateFmt(returnDateFmt)
                .setTrainPassengerViewModel(trainPassengerViewModel)
                .setPassengerFmt(passengerFmt)
                .setIsOneWay(isOneWay)
                .build();
    }

    private String buildPassengerTextFormatted(TrainPassengerViewModel passData) {
        String passengerFmt = "";
        if (passData.getAdult() > 0) {
            passengerFmt = passData.getAdult() + " Dewasa";
            if (passData.getInfant() > 0) {
                passengerFmt += ", " + passData.getInfant() + " Bayi";
            }
        }
        return passengerFmt;
    }

}
