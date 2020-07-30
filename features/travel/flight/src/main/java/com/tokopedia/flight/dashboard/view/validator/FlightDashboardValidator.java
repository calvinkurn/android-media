package com.tokopedia.flight.dashboard.view.validator;

import android.text.TextUtils;

import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.dashboard.view.fragment.model.FlightDashboardModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

/**
 * Created by alvarisi on 10/31/17.
 */

public class FlightDashboardValidator {

    @Inject
    public FlightDashboardValidator() {
    }

    public boolean validateDepartureNotEmtpty(FlightDashboardModel currentDashboardViewModel) {
        return currentDashboardViewModel.getDepartureAirport() != null;
    }

    public boolean validateArrivalNotEmpty(FlightDashboardModel currentDashboardViewModel) {
        return currentDashboardViewModel.getArrivalAirport() != null;
    }

    public boolean validateArrivalAndDestinationNotSame(FlightDashboardModel currentDashboardViewModel) {
        List<String> departureAirports = new ArrayList<>();
        if (!TextUtils.isEmpty(currentDashboardViewModel.getDepartureAirport().getAirportCode())) {
            departureAirports.add(currentDashboardViewModel.getDepartureAirport().getAirportCode());
        }

        if (currentDashboardViewModel.getDepartureAirport().getCityAirports() != null && currentDashboardViewModel.getDepartureAirport().getCityAirports().size() > 0) {
            departureAirports.addAll(currentDashboardViewModel.getDepartureAirport().getCityAirports());
        }
        List<String> arrivalAirports = new ArrayList<>();
        if (!TextUtils.isEmpty(currentDashboardViewModel.getArrivalAirport().getAirportCode())) {
            arrivalAirports.add(currentDashboardViewModel.getArrivalAirport().getAirportCode());
        }

        if (currentDashboardViewModel.getArrivalAirport().getCityAirports() != null && currentDashboardViewModel.getArrivalAirport().getCityAirports().size() > 0) {
            arrivalAirports.addAll(currentDashboardViewModel.getArrivalAirport().getCityAirports());
        }
        List<String> commons = new ArrayList<>(departureAirports);
        commons.retainAll(arrivalAirports);
        return commons.size() == 0;
    }

    public boolean validateDepartureDateAtLeastToday(FlightDashboardModel currentDashboardViewModel) {
        Calendar now = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));
        return removeTime(FlightDateUtil.stringToDate(currentDashboardViewModel.getDepartureDate())).compareTo(removeTime(now.getTime())) >= 0;
    }

    public boolean validateReturnDateShouldGreaterOrEqualDeparture(FlightDashboardModel currentDashboardViewModel) {
        return currentDashboardViewModel.isOneWay() || removeTime(FlightDateUtil.stringToDate(currentDashboardViewModel.getReturnDate())).compareTo(removeTime(FlightDateUtil.stringToDate(currentDashboardViewModel.getDepartureDate()))) >= 0;
    }

    public boolean validatePassengerAtLeastOneAdult(FlightDashboardModel currentDashboardViewModel) {
        return currentDashboardViewModel.getFlightPassengerViewModel() != null &&
                currentDashboardViewModel.getFlightPassengerViewModel().getAdult() > 0;
    }

    public boolean validateFlightClassNotEmpty(FlightDashboardModel currentDashboardViewModel) {
        return currentDashboardViewModel.getFlightClass() != null;
    }

    public Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public boolean validateAirportsShouldDifferentCity(FlightDashboardModel currentDashboardViewModel) {
        return currentDashboardViewModel.getArrivalAirport() != null && currentDashboardViewModel.getDepartureAirport() != null
                && !currentDashboardViewModel.getArrivalAirport().getCityName()
                .equalsIgnoreCase(currentDashboardViewModel.getDepartureAirport().getCityName());
    }
}
