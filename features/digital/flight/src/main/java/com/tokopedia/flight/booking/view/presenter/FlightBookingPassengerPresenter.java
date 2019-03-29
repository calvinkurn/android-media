package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightPassengerInfoValidator;
import com.tokopedia.flight.common.util.FlightPassengerTitleType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by alvarisi on 11/16/17.
 */

public class FlightBookingPassengerPresenter extends BaseDaggerPresenter<FlightBookingPassengerContract.View> implements FlightBookingPassengerContract.Presenter {

    private static final int DEFAULT_LAST_HOUR_IN_DAY = 23;
    private static final int DEFAULT_LAST_MIN_IN_DAY = 59;
    private static final int DEFAULT_LAST_SEC_IN_DAY = 59;
    private final int PLUS_ONE = 1;
    private final int MINUS_TWO = -2;
    private final int MINUS_TWELVE = -12;
    private final int PLUS_SIX = 6;
    private final int PLUS_TWENTY = 20;

    private FlightPassengerInfoValidator flightPassengerInfoValidator;

    @Inject
    public FlightBookingPassengerPresenter(FlightPassengerInfoValidator flightPassengerInfoValidator) {
        this.flightPassengerInfoValidator = flightPassengerInfoValidator;
    }

    @Override
    public void onViewCreated() {
        getView().renderHeaderTitle(getView().getCurrentPassengerViewModel().getHeaderTitle());

        if (isAdultPassenger()) {
            getView().renderHeaderSubtitle(R.string.flight_booking_passenger_adult_subtitle);
            getView().renderSpinnerForAdult();
            if (getView().isMandatoryDoB() || !getView().isDomestic()) {
                getView().showBirthdayInputView();
            } else {
                getView().hideBirthdayInputView();
            }
        } else {
            getView().renderSpinnerForChildAndInfant();
            getView().showBirthdayInputView();
            if (isChildPassenger()) {
                getView().renderHeaderSubtitle(R.string.flight_booking_passenger_child_subtitle);
            } else {
                getView().renderHeaderSubtitle(R.string.flight_booking_passenger_infant_subtitle);
            }
        }

        if (isAdultPassenger() || isChildPassenger()) {
            if (getView().getLuggageViewModels().size() > 0)
                getView().renderPassengerLuggages(getView().getLuggageViewModels(), getView().getCurrentPassengerViewModel().getFlightBookingLuggageMetaViewModels());
            if (getView().getMealViewModels().size() > 0)
                getView().renderPassengerMeals(getView().getMealViewModels(), getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels());
        }

        if (getView().getCurrentPassengerViewModel().getPassengerFirstName() != null) {
            getView().renderPassengerName(getView().getCurrentPassengerViewModel().getPassengerFirstName(),
                    getView().getCurrentPassengerViewModel().getPassengerLastName());
        }

        if (getView().getCurrentPassengerViewModel().getPassengerTitle() != null) {
            getView().renderPassengerTitle(getView().getCurrentPassengerViewModel().getPassengerTitle());
        }

        if (getView().getCurrentPassengerViewModel().getPassengerBirthdate() != null) {
            getView().renderBirthdate(FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getCurrentPassengerViewModel().getPassengerBirthdate()));
        }

        if (getView().getCurrentPassengerViewModel().getPassengerId() != null &&
                !getView().getCurrentPassengerViewModel().getPassengerId().equals("")) {
            getView().renderSelectedList(String.format("%s %s",
                    getView().getCurrentPassengerViewModel().getPassengerFirstName(),
                    getView().getCurrentPassengerViewModel().getPassengerLastName()
            ));
        }

        renderPassport();
    }

    private void renderPassport() {
        if (!getView().isDomestic()) {
            getView().showPassportContainer();
            if (getView().getCurrentPassengerViewModel().getPassportNumber() != null) {
                getView().renderPassportNumber(getView().getCurrentPassengerViewModel().getPassportNumber());
            } else {
                getView().renderPassportNumber("");
            }

            if (getView().getCurrentPassengerViewModel().getPassportExpiredDate() != null) {
                getView().renderPassportExpiredDate(FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT,
                        FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getCurrentPassengerViewModel().getPassportExpiredDate()));
            } else {
                getView().renderPassportExpiredDate("");
            }

            if (getView().getCurrentPassengerViewModel().getPassportNationality() != null) {
                getView().renderPassportNationality(getView().getCurrentPassengerViewModel()
                        .getPassportNationality().getCountryName());
            } else {
                getView().renderPassportNationality("");
            }

            if (getView().getCurrentPassengerViewModel().getPassportIssuerCountry() != null) {
                getView().renderPassportIssuerCountry(getView().getCurrentPassengerViewModel()
                        .getPassportIssuerCountry().getCountryName());
            } else {
                getView().renderPassportIssuerCountry("");
            }

        } else {
            getView().hidePassportContainer();
        }
    }

    @Override
    public void onSaveButtonClicked() {
        getView().hideKeyboard();
        if (validateFields(getView().getDepartureDateString())) {
            getView().getCurrentPassengerViewModel().setPassengerTitle(getView().getPassengerTitle());
            getView().getCurrentPassengerViewModel().setPassengerTitleId(getPassengerTitleId());
            getView().getCurrentPassengerViewModel().setPassengerFirstName(getView().getPassengerFirstName());
            getView().getCurrentPassengerViewModel().setPassengerBirthdate(
                    FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, FlightDateUtil.DEFAULT_FORMAT, getView().getPassengerBirthDate())
            );
            getView().getCurrentPassengerViewModel().setPassengerLastName(getView().getPassengerLastName());
            getView().getCurrentPassengerViewModel().setPassportNumber(getView().getPassportNumber());
            getView().navigateResultUpdatePassengerData(getView().getCurrentPassengerViewModel());
        }
    }

    @Override
    public void onBirthdateClicked() {

        Date maxDate, minDate = null, selectedDate;
        Date departureDate = FlightDateUtil.stringToDate(getView().getDepartureDateString());

        if (isChildPassenger()) {
            // minDate = 12 tahun + 1 hari
            minDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, -12);
            minDate = FlightDateUtil.addTimeToSpesificDate(minDate, Calendar.DATE, +1);
            maxDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, -2);
            selectedDate = maxDate;
        } else if (isAdultPassenger()) {
            maxDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, -12);
            selectedDate = maxDate;
        } else {
            // minDate = 2 tahun + 1 hari
            minDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, -2);
            minDate = FlightDateUtil.addTimeToSpesificDate(minDate, Calendar.DATE, +1);
            maxDate = FlightDateUtil.getCurrentDate();
            selectedDate = maxDate;
        }
        if (flightPassengerInfoValidator.validateBirthdateNotEmpty(getView().getPassengerBirthDate())) {
            selectedDate = FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getPassengerBirthDate());
        }

        Calendar maxDateCalendar = FlightDateUtil.getCurrentCalendar();
        maxDateCalendar.setTime(maxDate);
        maxDateCalendar.set(Calendar.HOUR_OF_DAY, DEFAULT_LAST_HOUR_IN_DAY);
        maxDateCalendar.set(Calendar.MINUTE, DEFAULT_LAST_MIN_IN_DAY);
        maxDateCalendar.set(Calendar.SECOND, DEFAULT_LAST_SEC_IN_DAY);

        if (minDate != null) {
            getView().showBirthdatePickerDialog(selectedDate, minDate, maxDateCalendar.getTime());
        } else {
            getView().showBirthdatePickerDialog(selectedDate, maxDateCalendar.getTime());
        }
    }

    @Override
    public void onBirthdateChange(int year, int month, int date, Date minDate, Date maxDate) {
        Calendar now = FlightDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, date);
        Date newReturnDate = now.getTime();

        //max Date + 1 hari, karena pengecekan pakai before
        maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, +1);

        if (flightPassengerInfoValidator.validateDateNotBetween(minDate, maxDate, newReturnDate)) {
            if (isChildPassenger()) {
                getView().showPassengerChildBirthdateShouldMoreThan2Years(R.string.flight_booking_passenger_birthdate_child_shoud_between_twelve_to_two_years);
            } else if (isInfantPassenger()) {
                getView().showPassengerInfantBirthdateShouldNoMoreThan2Years(R.string.flight_booking_passenger_birthdate_infant_should_no_more_than_two_years);
            }
        } else {
            String birthdateStr = FlightDateUtil.dateToString(newReturnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
            getView().renderBirthdate(birthdateStr);
        }

        getView().hideKeyboard();
    }

    @Override
    public void onBirthdateChange(int year, int month, int date, Date maxDate) {
        Calendar now = FlightDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, date);
        Date newReturnDate = now.getTime();

        //max Date + 1 hari, karena pengecekan pakai before
        maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, PLUS_ONE);

        if (flightPassengerInfoValidator.validateDateExceedMaxDate(maxDate, newReturnDate)) {
            getView().showPassengerAdultBirthdateShouldMoreThan12Years(R.string.flight_booking_passenger_birthdate_adult_shoud_more_than_twelve_years);
        } else {
            String birthdateStr = FlightDateUtil.dateToString(newReturnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
            getView().renderBirthdate(birthdateStr);
        }
        getView().hideKeyboard();
    }

    @Override
    public void onPassengerLuggageClick(FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel) {
        FlightBookingAmenityMetaViewModel existingSelected = null;
        for (FlightBookingAmenityMetaViewModel selected : getView().getCurrentPassengerViewModel().getFlightBookingLuggageMetaViewModels()) {
            if (selected.getKey().equalsIgnoreCase(flightBookingLuggageMetaViewModel.getKey())) {
                existingSelected = selected;
            }
        }
        if (existingSelected == null) {
            existingSelected = new FlightBookingAmenityMetaViewModel();
            existingSelected.setKey(flightBookingLuggageMetaViewModel.getKey());
            existingSelected.setJourneyId(flightBookingLuggageMetaViewModel.getJourneyId());
            existingSelected.setArrivalId(flightBookingLuggageMetaViewModel.getArrivalId());
            existingSelected.setDepartureId(flightBookingLuggageMetaViewModel.getDepartureId());
            existingSelected.setDescription(flightBookingLuggageMetaViewModel.getDescription());
            existingSelected.setAmenities(new ArrayList<FlightBookingAmenityViewModel>());
        }
        getView().navigateToLuggagePicker(flightBookingLuggageMetaViewModel.getAmenities(), existingSelected);
    }

    @Override
    public void onLuggageDataChange(FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel) {
        List<FlightBookingAmenityMetaViewModel> viewModels = getView().getCurrentPassengerViewModel().getFlightBookingLuggageMetaViewModels();
        int index = viewModels.indexOf(flightBookingLuggageMetaViewModel);

        if (flightBookingLuggageMetaViewModel.getAmenities().size() != 0) {
            if (index != -1) {
                viewModels.set(index, flightBookingLuggageMetaViewModel);
            } else {
                viewModels.add(flightBookingLuggageMetaViewModel);
            }
        } else {
            if (index != -1) {
                viewModels.remove(index);
            }
        }

        getView().renderPassengerLuggages(getView().getLuggageViewModels(), viewModels);
    }

    @Override
    public void onMealDataChange(FlightBookingAmenityMetaViewModel flightBookingAmenityMetaViewModel) {
        List<FlightBookingAmenityMetaViewModel> viewModels = getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels();
        int index = viewModels.indexOf(flightBookingAmenityMetaViewModel);

        if (flightBookingAmenityMetaViewModel.getAmenities().size() != 0) {
            if (index != -1) {
                viewModels.set(index, flightBookingAmenityMetaViewModel);
            } else {
                viewModels.add(flightBookingAmenityMetaViewModel);
            }
        } else {
            if (index != -1) {
                viewModels.remove(index);
            }
        }

        getView().renderPassengerMeals(getView().getMealViewModels(), viewModels);
    }

    @Override
    public void onSavedPassengerClicked() {
        FlightBookingPassengerViewModel existingSelected = getView().getCurrentPassengerViewModel();

        if (existingSelected == null) {
            existingSelected = new FlightBookingPassengerViewModel();
        }

        getView().navigateToSavedPassengerPicker(existingSelected);
    }

    @Override
    public void onNewPassengerChoosed() {
        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getView().getCurrentPassengerViewModel();
        flightBookingPassengerViewModel.setPassengerId("");
        flightBookingPassengerViewModel.setPassengerFirstName("");
        flightBookingPassengerViewModel.setPassengerLastName("");
        flightBookingPassengerViewModel.setPassengerBirthdate("");
        flightBookingPassengerViewModel.setPassengerTitle("");

        getView().renderSelectedList(getView().getString(R.string.flight_booking_passenger_saved_secondary_hint));
        getView().renderPassengerName("", "");
        getView().renderBirthdate("");
    }

    @Override
    public void onChangeFromSavedPassenger(FlightBookingPassengerViewModel selectedPassenger) {
        Date sixMonthFromDeparture = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(getView().getDepartureDateString()),
                Calendar.MONTH, PLUS_SIX);
        Date twentyYearsFromDeparture = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(getView().getDepartureDateString()),
                Calendar.YEAR, PLUS_TWENTY);

        getView().renderSelectedList(String.format("%s %s",
                selectedPassenger.getPassengerFirstName(),
                selectedPassenger.getPassengerLastName()));

        FlightBookingPassengerViewModel currentPassengerViewModel = getView().getCurrentPassengerViewModel();
        currentPassengerViewModel.setPassengerId(selectedPassenger.getPassengerId());
        currentPassengerViewModel.setPassengerFirstName(selectedPassenger.getPassengerFirstName());
        currentPassengerViewModel.setPassengerLastName(selectedPassenger.getPassengerLastName());

        if ((isChildPassenger() || isInfantPassenger()) &&
                selectedPassenger.getPassengerTitleId() == FlightPassengerTitleType.NYONYA) {
            currentPassengerViewModel.setPassengerTitle(getView().getMissTitle());
            currentPassengerViewModel.setPassengerTitleId(FlightPassengerTitleType.NONA);
        } else {
            currentPassengerViewModel.setPassengerTitle(selectedPassenger.getPassengerTitle());
            currentPassengerViewModel.setPassengerTitleId(selectedPassenger.getPassengerTitleId());
        }

        currentPassengerViewModel.setPassportNumber(selectedPassenger.getPassportNumber());
        currentPassengerViewModel.setPassportNationality(selectedPassenger.getPassportNationality());
        currentPassengerViewModel.setPassportIssuerCountry(selectedPassenger.getPassportIssuerCountry());

        if (flightPassengerInfoValidator.validateBirthdateNotEmpty(selectedPassenger.getPassengerBirthdate()) &&
                (isChildPassenger() || isInfantPassenger() || getView().isMandatoryDoB() || !getView().isDomestic())) {
            currentPassengerViewModel.setPassengerBirthdate(selectedPassenger.getPassengerBirthdate());
        }

        if (selectedPassenger.getPassportExpiredDate() != null && !selectedPassenger.getPassportExpiredDate().isEmpty() &&
                selectedPassenger.getPassportExpiredDate().length() > 0 && flightPassengerInfoValidator
                .validateExpiredDateOfPassportAtLeast6Month(FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT,
                        FlightDateUtil.DEFAULT_VIEW_FORMAT, selectedPassenger.getPassportExpiredDate()),
                        sixMonthFromDeparture) && flightPassengerInfoValidator.validateExpiredDateOfPassportMax20Years(
                FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT,
                        selectedPassenger.getPassportExpiredDate()), twentyYearsFromDeparture)) {
            currentPassengerViewModel.setPassportExpiredDate(selectedPassenger.getPassportExpiredDate());
        } else {
            currentPassengerViewModel.setPassportExpiredDate(null);
        }

        getView().setCurrentPassengerViewModel(currentPassengerViewModel);
        onViewCreated();
    }

    @Override
    public void onDeleteMeal(FlightBookingAmenityMetaViewModel viewModel) {
        List<FlightBookingAmenityMetaViewModel> viewModels = getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels();
        int index = viewModels.indexOf(viewModel);
        if (index != -1) {
            viewModels.set(index, viewModel);
        } else {
            viewModels.add(viewModel);
        }

        getView().renderPassengerMeals(getView().getMealViewModels(), viewModels);
    }

    @Override
    public void onOptionMeal(FlightBookingAmenityMetaViewModel viewModel) {
        FlightBookingAmenityMetaViewModel existingSelected = null;
        for (FlightBookingAmenityMetaViewModel viewModel1 : getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels()) {
            if (viewModel1.getKey().equalsIgnoreCase(viewModel.getKey())) {
                existingSelected = viewModel1;
                break;
            }
        }

        if (existingSelected == null) {
            existingSelected = new FlightBookingAmenityMetaViewModel();
            existingSelected.setKey(viewModel.getKey());
            existingSelected.setJourneyId(viewModel.getJourneyId());
            existingSelected.setArrivalId(viewModel.getArrivalId());
            existingSelected.setDepartureId(viewModel.getDepartureId());
            existingSelected.setAmenities(new ArrayList<FlightBookingAmenityViewModel>());
            existingSelected.setDescription(viewModel.getDescription());
        }
        getView().navigateToMealPicker(viewModel.getAmenities(), existingSelected);
    }

    @Override
    public void onPassportExpiredClicked() {

        Date minDate, selectedDate, maxDate;
        Date departureDate = FlightDateUtil.stringToDate(getView().getDepartureDateString());

        minDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.MONTH, PLUS_SIX);
        maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, PLUS_TWENTY);
        selectedDate = minDate;

        if (getView().getPassportExpiredDate().length() != 0 &&
                flightPassengerInfoValidator.validateExpiredDateOfPassportAtLeast6Month(getView()
                        .getPassportExpiredDate(), minDate) && flightPassengerInfoValidator.validateExpiredDateOfPassportMax20Years(
                getView().getPassportExpiredDate(), maxDate)) {
            selectedDate = FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getPassportExpiredDate());
        }

        getView().showPassportExpiredDatePickerDialog(selectedDate, minDate, maxDate);
    }

    @Override
    public void onPassportExpiredDateChanged(int year, int month, int dayOfMonth, Date minDate, Date maxDate) {
        Calendar now = FlightDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, dayOfMonth);
        Date expiredDate = now.getTime();

        if (!flightPassengerInfoValidator.validateExpiredDateOfPassportAtLeast6Month(FlightDateUtil
                .dateToString(expiredDate, FlightDateUtil.DEFAULT_VIEW_FORMAT), minDate)) {
            getView().showPassportExpiredDateShouldMoreThan6MonthsFromDeparture(
                    R.string.flight_passenger_passport_expired_date_less_than_6_month_error,
                    FlightDateUtil.dateToString(minDate, FlightDateUtil.DEFAULT_VIEW_FORMAT));
        } else if (!flightPassengerInfoValidator.validateExpiredDateOfPassportMax20Years(FlightDateUtil
                .dateToString(expiredDate, FlightDateUtil.DEFAULT_VIEW_FORMAT), maxDate)) {
            getView().showPassportExpiredDateMax20Years(
                    R.string.flight_passenger_passport_expired_date_more_than_20_year_error,
                    FlightDateUtil.dateToString(maxDate, FlightDateUtil.DEFAULT_VIEW_FORMAT));
        } else {
            String expiredDateStr = FlightDateUtil.dateToString(expiredDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
            getView().getCurrentPassengerViewModel().setPassportExpiredDate(
                    FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT,
                            FlightDateUtil.DEFAULT_FORMAT, expiredDateStr));
            getView().renderPassportExpiredDate(expiredDateStr);
        }
        getView().hideKeyboard();
    }

    @Override
    public void onNationalityChanged(FlightBookingPhoneCodeViewModel flightPassportNationalityViewModel) {
        getView().getCurrentPassengerViewModel().setPassportNationality(flightPassportNationalityViewModel);
        getView().renderPassportNationality(flightPassportNationalityViewModel.getCountryName());
    }

    @Override
    public void onIssuerCountryChanged(FlightBookingPhoneCodeViewModel flightPassportIssuerCountry) {
        getView().getCurrentPassengerViewModel().setPassportIssuerCountry(flightPassportIssuerCountry);
        getView().renderPassportIssuerCountry(flightPassportIssuerCountry.getCountryName());
    }

    private boolean validateFields(String departureDateString) {
        boolean isValid = true;
        boolean isNeedPassport = !getView().isDomestic();
        Date twelveYearsAgo = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(departureDateString), Calendar.YEAR, MINUS_TWELVE);
        Date twoYearsAgo = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(departureDateString), Calendar.YEAR, MINUS_TWO);
        Date sixMonthFromDeparture = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(departureDateString),
                Calendar.MONTH, PLUS_SIX);
        Date twentyYearsFromToday = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, PLUS_TWENTY);

        if (flightPassengerInfoValidator.validateNameIsEmpty(getView().getPassengerFirstName())) {
            isValid = false;
            getView().showPassengerNameEmptyError(R.string.flight_booking_passenger_first_name_empty_error);
        } else if (flightPassengerInfoValidator.validateNameIsNotAlphabetAndSpaceOnly(getView().getPassengerFirstName())) {
            isValid = false;
            getView().showPassengerFirstNameShouldAlphabetAndSpaceOnlyError(R.string.flight_booking_passenger_first_name_alpha_space_error);
        } else if (flightPassengerInfoValidator.validateNameIsMoreThanMaxLength(
                getView().getPassengerFirstName(), getView().getPassengerLastName())) {
            isValid = false;
            getView().showPassengerFirstNameShouldNoMoreThanMaxError(R.string.flight_booking_passenger_first_last_name_max_error);
        } else if (flightPassengerInfoValidator.validateNameIsEmpty(getView().getPassengerLastName())) {
            isValid = false;
            getView().showPassengerLastNameShouldSameWithFirstNameError(R.string.flight_booking_passenger_last_name_should_same_error);
        } else if (flightPassengerInfoValidator.validateLastNameIsLessThanMinLength(getView().getPassengerLastName())) {
            isValid = false;
            getView().showPassengerLastNameEmptyError(R.string.flight_booking_passenger_last_name_empty_error);
        } else if (flightPassengerInfoValidator.validateLastNameIsNotSingleWord(getView().getPassengerLastName())) {
            isValid = false;
            getView().showPassengerLastNameShouldOneWordError(R.string.flight_booking_passenger_last_name_single_word_error);
        } else if (flightPassengerInfoValidator.validateNameIsNotAlphabetAndSpaceOnly(getView().getPassengerLastName())) {
            isValid = false;
            getView().showPassengerLastNameShouldAlphabetAndSpaceOnlyError(R.string.flight_booking_passenger_last_name_alpha_space_error);
        } else if (flightPassengerInfoValidator.validateTitleIsEmpty(getView().getPassengerTitle())) {
            isValid = false;
            getView().showPassengerTitleEmptyError(R.string.flight_bookingpassenger_title_error);
        } else if ((isChildPassenger() || isInfantPassenger()) &&
                !flightPassengerInfoValidator.validateBirthdateNotEmpty(getView().getPassengerBirthDate())) {
            isValid = false;
            getView().showPassengerBirthdateEmptyError(R.string.flight_booking_passenger_birthdate_empty_error);
        } else if ((isAdultPassenger()) && !flightPassengerInfoValidator.validateBirthdateNotEmpty(
                getView().getPassengerBirthDate()) && (getView().isMandatoryDoB() || !getView().isDomestic())) {
            isValid = false;
            getView().showPassengerBirthdateEmptyError(R.string.flight_booking_passenger_birthdate_empty_error);
        } else if (isAdultPassenger() && flightPassengerInfoValidator.validateBirthdateNotEmpty(
                getView().getPassengerBirthDate()) &&  (getView().isMandatoryDoB() || !getView().isDomestic()) &&
                flightPassengerInfoValidator.validateDateMoreThan(getView().getPassengerBirthDate(), twelveYearsAgo)) {
            isValid = false;
            getView().showPassengerAdultBirthdateShouldMoreThan12Years(R.string.flight_booking_passenger_birthdate_adult_shoud_more_than_twelve_years);
        } else if (isChildPassenger() && flightPassengerInfoValidator.validateDateMoreThan(
                getView().getPassengerBirthDate(), twoYearsAgo)) {
            isValid = false;
            getView().showPassengerChildBirthdateShouldMoreThan2Years(R.string.flight_booking_passenger_birthdate_child_shoud_more_than_two_years);
        } else if (isChildPassenger() && flightPassengerInfoValidator.validateDateNotLessThan(
                twelveYearsAgo,
                getView().getPassengerBirthDate())) {
            isValid = false;
            getView().showPassengerChildBirthdateShouldLessThanEqual12Years(R.string.flight_booking_passenger_birthdate_child_sholud_lessthan_than_equal_12years);
        } else if (isInfantPassenger() && flightPassengerInfoValidator.validateDateLessThan(
                getView().getPassengerBirthDate(), twoYearsAgo)) {
            isValid = false;
            getView().showPassengerInfantBirthdateShouldNoMoreThan2Years(R.string.flight_booking_passenger_birthdate_infant_should_no_more_than_two_years);
        } else if (isNeedPassport && !flightPassengerInfoValidator.validatePassportNumberNotEmpty(getView().getPassportNumber())) {
            isValid = false;
            getView().showPassengerPassportNumberEmptyError(R.string.flight_booking_passport_number_empty_error);
        } else if (isNeedPassport && !flightPassengerInfoValidator.validatePassportNumberAlphaNumeric(getView().getPassportNumber())) {
            isValid = false;
            getView().showPassengerPassportNumberShouldAlphaNumericError(R.string.flight_booking_passport_number_alphanumeric_error);
        } else if (isNeedPassport && getView().getCurrentPassengerViewModel().getPassportExpiredDate() == null) {
            isValid = false;
            getView().showPassengerPassportExpiredDateEmptyError(R.string.flight_booking_passport_expired_date_empty_error);
        } else if (isNeedPassport && !flightPassengerInfoValidator.validateExpiredDateOfPassportAtLeast6Month(
                getView().getPassportExpiredDate(), sixMonthFromDeparture)) {
            isValid = false;
            getView().showPassportExpiredDateShouldMoreThan6MonthsFromDeparture(
                    R.string.flight_passenger_passport_expired_date_less_than_6_month_error,
                    FlightDateUtil.dateToString(sixMonthFromDeparture, FlightDateUtil.DEFAULT_VIEW_FORMAT));
        } else if (isNeedPassport && !flightPassengerInfoValidator.validateExpiredDateOfPassportMax20Years(
                getView().getPassportExpiredDate(), twentyYearsFromToday)) {
            getView().showPassportExpiredDateMax20Years(
                    R.string.flight_passenger_passport_expired_date_more_than_20_year_error,
                    FlightDateUtil.dateToString(twentyYearsFromToday, FlightDateUtil.DEFAULT_VIEW_FORMAT));
        } else if (isNeedPassport && getView().getCurrentPassengerViewModel().getPassportNationality() == null) {
            isValid = false;
            getView().showPassportNationalityEmptyError(R.string.flight_booking_passport_nationality_empty_error);
        } else if (isNeedPassport && getView().getCurrentPassengerViewModel().getPassportIssuerCountry() == null) {
            isValid = false;
            getView().showPassportIssuerCountryEmptyError(R.string.flight_booking_passport_issuer_country_empty_error);
        }

        return isValid;
    }

    private boolean isAdultPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == FlightBookingPassenger.ADULT;
    }

    private boolean isChildPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == FlightBookingPassenger.CHILDREN;
    }

    private boolean isInfantPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == FlightBookingPassenger.INFANT;
    }

    private int getPassengerTitleId() {
        switch (getView().getTitleSpinnerPosition()) {
            case 1:
                return FlightPassengerTitleType.TUAN;
            case 2:
                if (isChildPassenger() || isInfantPassenger()) {
                    return FlightPassengerTitleType.NONA;
                } else {
                    return FlightPassengerTitleType.NYONYA;
                }
            case 3:
                return FlightPassengerTitleType.NONA;
            default:
                return 0;
        }
    }
}
