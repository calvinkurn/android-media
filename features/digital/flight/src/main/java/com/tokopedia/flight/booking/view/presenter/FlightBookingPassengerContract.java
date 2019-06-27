package com.tokopedia.flight.booking.view.presenter;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;

import java.util.Date;
import java.util.List;

/**
 * Created by alvarisi on 11/16/17.
 */

public interface FlightBookingPassengerContract {

    interface View extends CustomerView {

        FlightBookingPassengerViewModel getCurrentPassengerViewModel();

        void setCurrentPassengerViewModel(FlightBookingPassengerViewModel flightBookingPassengerViewModel);

        void renderSpinnerForAdult();

        void renderSpinnerForChildAndInfant();

        String getReturnTripId();

        String getDepartureId();

        boolean isDomestic();

        List<FlightBookingAmenityMetaViewModel> getLuggageViewModels();

        List<FlightBookingAmenityMetaViewModel> getMealViewModels();

        void renderPassengerMeals(List<FlightBookingAmenityMetaViewModel> flightBookingMealRouteViewModels, List<FlightBookingAmenityMetaViewModel> selecteds);

        void renderPassengerLuggages(List<FlightBookingAmenityMetaViewModel> flightBookingLuggageRouteViewModels, List<FlightBookingAmenityMetaViewModel> selecteds);

        void hideBirthdayInputView();

        void showBirthdayInputView();

        void renderHeaderTitle(String headerTitle);

        void renderHeaderSubtitle(@StringRes int resId);

        String getPassengerFirstName();

        void showPassengerNameEmptyError(@StringRes int resId);

        String getPassengerTitle();

        void showPassengerTitleEmptyError(@StringRes int resId);

        String getPassengerBirthDate();

        String getPassportExpiredDate();

        String getPassportNumber();

        void showPassengerBirthdateEmptyError(int resId);

        void showPassengerChildBirthdateShouldMoreThan2Years(int resId);

        void showPassengerInfantBirthdateShouldNoMoreThan2Years(int resID);

        void showPassengerAdultBirthdateShouldMoreThan12Years(int resID);

        void navigateResultUpdatePassengerData(FlightBookingPassengerViewModel currentPassengerViewModel);

        void showBirthdatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

        void showBirthdatePickerDialog(Date selectedDate, Date maxDate);

        void renderBirthdate(String birthdateStr);

        void renderPassengerName(String passengerName, String passengerLastName);

        void renderPassengerTitle(String passengerTitle);

        void renderSelectedList(String passengerName);

        void renderPassportExpiredDate(String expiredDateStr);

        void navigateToLuggagePicker(List<FlightBookingAmenityViewModel> luggages, FlightBookingAmenityMetaViewModel selected);

        void navigateToMealPicker(List<FlightBookingAmenityViewModel> viewModel, FlightBookingAmenityMetaViewModel selected);

        void navigateToSavedPassengerPicker(FlightBookingPassengerViewModel selected);

        int getTitleSpinnerPosition();

        void showPassengerFirstNameShouldNoMoreThanMaxError(@StringRes int resId);

        void showPassengerLastNameEmptyError(@StringRes int resId);

        void showPassengerLastNameShouldNoMoreThanMaxError(@StringRes int resId);

        String getPassengerLastName();

        void showPassengerLastNameShouldOneWordError(@StringRes int resId);

        void showPassengerFirstNameShouldAlphabetAndSpaceOnlyError(@StringRes int resId);

        void showPassengerLastNameShouldAlphabetAndSpaceOnlyError(@StringRes int resId);

        void hideKeyboard();

        void hidePassportContainer();

        void showPassportContainer();

        boolean isMandatoryDoB();

        String getDepartureDateString();

        void showPassengerLastNameShouldSameWithFirstNameError(int resId);

        String getString(int resId);

        void showPassengerChildBirthdateShouldLessThanEqual12Years(int resId);

        void showPassportExpiredDatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

        void showPassportExpiredDateShouldMoreThan6MonthsFromDeparture(@StringRes int resId, String dateAfterSixMonth);

        void showPassportExpiredDateMax20Years(@StringRes int resId, String dateAfterTwentyYears);

        void renderPassportNationality(String countryName);

        void renderPassportIssuerCountry(String countryName);

        void renderPassportNumber(String passportNumber);

        void showPassengerPassportNumberEmptyError(@StringRes int resId);

        void showPassportNationalityEmptyError(@StringRes int resId);

        void showPassportIssuerCountryEmptyError(@StringRes int resId);

        void showPassengerPassportExpiredDateEmptyError(@StringRes int resId);

        void showPassengerPassportNumberShouldAlphaNumericError(@StringRes int resId);

        String getMissTitle();

    }

    interface Presenter extends CustomerPresenter<View> {

        void onViewCreated();

        void onSaveButtonClicked();

        void onBirthdateClicked();

        void onBirthdateChange(int year, int month, int date, Date minDate, Date maxDate);

        void onBirthdateChange(int year, int month, int date, Date maxDate);

        void onPassengerLuggageClick(FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel);

        void onLuggageDataChange(FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel);

        void onDeleteMeal(FlightBookingAmenityMetaViewModel viewModel);

        void onOptionMeal(FlightBookingAmenityMetaViewModel viewModel);

        void onMealDataChange(FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel);

        void onSavedPassengerClicked();

        void onNewPassengerChoosed();

        void onChangeFromSavedPassenger(FlightBookingPassengerViewModel selectedPassenger);

        void onPassportExpiredClicked();

        void onPassportExpiredDateChanged(int year, int month, int dayOfMonth, Date minDate, Date maxxDate);

        void onNationalityChanged(FlightBookingPhoneCodeViewModel flightPassportNationalityViewModel);

        void onIssuerCountryChanged(FlightBookingPhoneCodeViewModel flightPassportIssuerCountry);
    }
}
