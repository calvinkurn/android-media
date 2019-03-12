package com.tokopedia.flight.dashboard.view.validator;

import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardViewModel;

import java.util.ArrayList;
import java.util.Arrays;
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

    public boolean validateDepartureNotEmtpty(FlightDashboardViewModel currentDashboardViewModel) {
        return currentDashboardViewModel.getDepartureAirport() != null;
    }

    public boolean validateArrivalNotEmpty(FlightDashboardViewModel currentDashboardViewModel) {
        return currentDashboardViewModel.getArrivalAirport() != null;
    }

    public boolean validateArrivalAndDestinationNotSame(FlightDashboardViewModel currentDashboardViewModel) {
        List<String> departureAirports = new ArrayList<>();
        if (currentDashboardViewModel.getDepartureAirport().getAirportCode() != null) {
            departureAirports.add(currentDashboardViewModel.getDepartureAirport().getAirportCode());
        }

        if (currentDashboardViewModel.getDepartureAirport().getCityAirports() != null && currentDashboardViewModel.getDepartureAirport().getCityAirports().size() > 0) {
            departureAirports.addAll(currentDashboardViewModel.getDepartureAirport().getCityAirports());
        }
        List<String> arrivalAirports = new ArrayList<>();
        if (currentDashboardViewModel.getArrivalAirport().getAirportCode() != null) {
            arrivalAirports.add(currentDashboardViewModel.getArrivalAirport().getAirportCode());
        }

        if (currentDashboardViewModel.getArrivalAirport().getCityAirports() != null && currentDashboardViewModel.getArrivalAirport().getCityAirports().size() > 0) {
            arrivalAirports.addAll(currentDashboardViewModel.getArrivalAirport().getCityAirports());
        }
        List<String> commons = new ArrayList<>(departureAirports);
        commons.retainAll(arrivalAirports);
        return commons.size() == 0;
    }

    public boolean validateDepartureDateAtLeastToday(FlightDashboardViewModel currentDashboardViewModel) {
        Calendar now = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));
        return removeTime(FlightDateUtil.stringToDate(currentDashboardViewModel.getDepartureDate())).compareTo(removeTime(now.getTime())) >= 0;
    }

    public boolean validateReturnDateShouldGreaterOrEqualDeparture(FlightDashboardViewModel currentDashboardViewModel) {
        return currentDashboardViewModel.isOneWay() || removeTime(FlightDateUtil.stringToDate(currentDashboardViewModel.getReturnDate())).compareTo(removeTime(FlightDateUtil.stringToDate(currentDashboardViewModel.getDepartureDate()))) >= 0;
    }

    public boolean validatePassengerAtLeastOneAdult(FlightDashboardViewModel currentDashboardViewModel) {
        return currentDashboardViewModel.getFlightPassengerViewModel() != null &&
                currentDashboardViewModel.getFlightPassengerViewModel().getAdult() > 0;
    }

    public boolean validateFlightClassNotEmpty(FlightDashboardViewModel currentDashboardViewModel) {
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

    public boolean validateAirportsShouldDifferentCity(FlightDashboardViewModel currentDashboardViewModel) {
        return currentDashboardViewModel.getArrivalAirport() != null && currentDashboardViewModel.getDepartureAirport() != null
                && !currentDashboardViewModel.getArrivalAirport().getCityId()
                .equalsIgnoreCase(currentDashboardViewModel.getDepartureAirport().getCityId());
    }
}
