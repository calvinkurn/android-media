package com.tokopedia.flight.passenger.view.presenter;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode;

import java.util.Date;

/**
 * @author by furqan on 12/03/18.
 */

public interface FlightPassengerUpdateContract {

    interface View extends CustomerView {

        String getString(int resId);

        String getDepartureDate();

        FlightBookingPassengerViewModel getCurrentPassengerViewModel();

        String getPassengerBirthdate();

        String getPassengerFirstName();

        String getPassengerLastName();

        String getPassengerTitle();

        int getPassengerTitlePosition();

        String getRequestId();

        boolean getIsDomestic();

        String getPassportNumber();

        String getPassportExpiredDate();

        void setPassengerViewModel(FlightBookingPassengerViewModel flightBookingPassengerViewModel);

        void renderSpinnerForAdult();

        void renderSpinnerForChildAndInfant();

        void renderSelectedTitle(String passengerTitle);

        void renderPassengerType(String typeText);

        void renderPassengerName(String firstName, String lastName);

        void renderPassengerBirthdate(String birthdate);

        void hidePassportContainer();

        void showPassportContainer();

        void showBirthdatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

        void showBirthdatePickerDialog(Date selectedDate, Date maxDate);

        void showPassengerChildBirthdateShouldMoreThan2Years(int resId);

        void showPassengerInfantBirthdateShouldNoMoreThan2Years(int resId);

        void showPassengerAdultBirthdateShouldMoreThan12Years(int resId);

        void showPassengerNameEmptyError(int resId);

        void showPassengerFirstNameShouldAlphabetAndSpaceOnlyError(int resId);

        void showPassengerFirstNameShouldNoMoreThanMaxError(int resId);

        void showPassengerLastNameShouldSameWithFirstNameError(int resId);

        void showPassengerLastNameEmptyError(int resId);

        void showPassengerLastNameShouldOneWordError(int resId);

        void showPassengerLastNameShouldAlphabetAndSpaceOnlyError(int resId);

        void showPassengerTitleEmptyError(int resId);

        void showPassengerBirthdateEmptyError(int resId);

        void showPassengerPassportNumberEmptyError(@StringRes int resId);

        void showPassengerPassportExpiredDateEmptyError(@StringRes int resId);

        void showPassportNationalityEmptyError(@StringRes int resId);

        void showPassportIssuerCountryEmptyError(@StringRes int resId);

        void showPassportExpiredDateShouldMoreThan6MonthsFromDeparture(@StringRes int resId, String dateAfterSixMonth);

        void showPassportExpiredDateMax20Years(@StringRes int resId, String dateAfterTwentyYears);

        void showPassportExpiredDatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

        void hideKeyboard();

        void onSuccessUpdatePassengerData();

        void onErrorUpdatePassengerData();

        void renderPassportNumber(String passportNumber);

        void renderPassportExpiredDate(String passportExpiredDate);

        void renderPassportNationality(String passportNationality);

        void renderPassportIssuerCountry(String passportIssuerCountry);
    }

    interface Presenter {

        void onViewCreated();

        void onSaveButtonClicked();

        void onBirthdateClicked();

        void onBirthdateChanged(int year, int month, int date, Date minDate, Date maxDate);

        void onBirthdateChanged(int year, int month, int date, Date maxDate);

        void onNationalityChanged(CountryPhoneCode flightPassportNationalityViewModel);

        void onIssuerCountryChanged(CountryPhoneCode flightPassportIssuerCountry);

        void onPassportExpiredClicked();

        void onPassportExpiredDateChanged(int year, int month, int dayOfMonth, Date minDate, Date maxDate);
    }

}
