package com.tokopedia.flight.passenger.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightPassengerInfoValidator;
import com.tokopedia.flight.passenger.domain.FlightPassengerUpdateDataUseCase;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.ADULT;
import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.CHILDREN;
import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.INFANT;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NONA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NYONYA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.TUAN;

/**
 * @author by furqan on 12/03/18.
 */

public class FlightPassengerUpdatePresenter extends BaseDaggerPresenter<FlightPassengerUpdateContract.View>
        implements FlightPassengerUpdateContract.Presenter {

    private static final int MINUS_TWELVE = -12;
    private static final int MINUS_TWO = -2;
    private static final int MINUS_ONE = -1;
    private static final int PLUS_ONE = 1;
    private static final int PLUS_SIX = 6;
    private final int PLUS_TWENTY = 20;

    private FlightPassengerInfoValidator flightPassengerInfoValidator;
    private FlightPassengerUpdateDataUseCase flightPassengerUpdateDataUseCase;

    @Inject
    public FlightPassengerUpdatePresenter(FlightPassengerInfoValidator flightPassengerInfoValidator,
                                          FlightPassengerUpdateDataUseCase flightPassengerUpdateDataUseCase) {
        this.flightPassengerInfoValidator = flightPassengerInfoValidator;
        this.flightPassengerUpdateDataUseCase = flightPassengerUpdateDataUseCase;
    }

    @Override
    public void onViewCreated() {
        renderView();
    }

    @Override
    public void onSaveButtonClicked() {

        if (validateFields()) {
            updatePassengerData();
        }

    }

    @Override
    public void onBirthdateClicked() {

        Date maxDate, minDate = null, selectedDate;
        Date departureDate = FlightDateUtil.stringToDate(getView().getDepartureDate());

        if (isChildPassenger()) {
            minDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, MINUS_TWELVE);
            minDate = FlightDateUtil.addTimeToSpesificDate(minDate, Calendar.DATE, PLUS_ONE);
            maxDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, MINUS_TWO);
        } else if (isAdultPassenger()) {
            maxDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, MINUS_TWELVE);
        } else {
            minDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, MINUS_TWO);
            minDate = FlightDateUtil.addTimeToSpesificDate(minDate, Calendar.DATE, PLUS_ONE);
            maxDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.DATE, MINUS_ONE);
        }

        if (flightPassengerInfoValidator.validateBirthdateNotEmpty(getView().getPassengerBirthdate())) {
            selectedDate = FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT,
                    getView().getPassengerBirthdate());
        } else {
            selectedDate = maxDate;
        }

        if (minDate != null) {
            getView().showBirthdatePickerDialog(selectedDate, minDate, maxDate);
        } else {
            getView().showBirthdatePickerDialog(selectedDate, maxDate);
        }
    }

    @Override
    public void onBirthdateChanged(int year, int month, int date, Date minDate, Date maxDate) {
        Calendar now = FlightDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, date);
        Date newReturnDate = now.getTime();

        //max Date + 1 hari, karena pengecekan pakai before
        maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, PLUS_ONE);

        if (flightPassengerInfoValidator.validateDateNotBetween(minDate, maxDate, newReturnDate)) {
            if (isChildPassenger()) {
                getView().showPassengerChildBirthdateShouldMoreThan2Years(R.string.flight_booking_passenger_birthdate_child_shoud_more_than_two_years);
            } else if (isInfantPassenger()) {
                getView().showPassengerInfantBirthdateShouldNoMoreThan2Years(R.string.flight_booking_passenger_birthdate_infant_should_no_more_than_two_years);
            }
        } else {
            String birthdateStr = FlightDateUtil.dateToString(newReturnDate,
                    FlightDateUtil.DEFAULT_VIEW_FORMAT);
            getView().renderPassengerBirthdate(birthdateStr);
        }
    }

    @Override
    public void onBirthdateChanged(int year, int month, int date, Date maxDate) {
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
            String birthdateStr = FlightDateUtil.dateToString(newReturnDate,
                    FlightDateUtil.DEFAULT_VIEW_FORMAT);
            getView().renderPassengerBirthdate(birthdateStr);
        }
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

    @Override
    public void onPassportExpiredClicked() {
        Date minDate, selectedDate, maxDate;
        Date departureDate = FlightDateUtil.stringToDate(getView().getDepartureDate());

        minDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.MONTH, PLUS_SIX);
        maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, PLUS_TWENTY);
        selectedDate = minDate;

        if (getView().getPassportExpiredDate().length() != 0 &&
                flightPassengerInfoValidator.validateExpiredDateOfPassportAtLeast6Month(getView().getPassportExpiredDate(), departureDate)) {
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

    private void updatePassengerData() {
        flightPassengerUpdateDataUseCase.execute(
                flightPassengerUpdateDataUseCase.generateRequestParams(
                        getView().getCurrentPassengerViewModel().getPassengerId(),
                        getTitleId(),
                        getView().getPassengerFirstName(),
                        getView().getPassengerLastName(),
                        FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT,
                                FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                                getView().getPassengerBirthdate()),
                        getView().getPassportNumber(),
                        FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT,
                                FlightDateUtil.DEFAULT_FORMAT,
                                getView().getPassportExpiredDate()),
                        (getView().getCurrentPassengerViewModel().getPassportNationality() != null) ?
                                getView().getCurrentPassengerViewModel().getPassportNationality()
                                    .getCountryId() : null,
                        (getView().getCurrentPassengerViewModel().getPassportIssuerCountry() != null) ?
                                getView().getCurrentPassengerViewModel().getPassportIssuerCountry()
                                    .getCountryId() : null,
                        getView().getRequestId()
                ),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        getView().onErrorUpdatePassengerData();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            getView().onSuccessUpdatePassengerData();
                        } else {
                            getView().onErrorUpdatePassengerData();
                        }
                    }
                }
        );
    }

    private void renderView() {

        if (getView().getIsDomestic()) {
            getView().hidePassportContainer();
        } else {
            getView().showPassportContainer();
        }

        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getView().getCurrentPassengerViewModel();
        flightBookingPassengerViewModel.setPassengerTitle(flightBookingPassengerViewModel.getPassengerTitle());

        if (isAdultPassenger()) {
            getView().renderSpinnerForAdult();
            getView().renderPassengerType(
                    getView().getString(R.string.flightbooking_price_adult_label));
        } else {
            getView().renderSpinnerForChildAndInfant();

            if (isChildPassenger()) {
                getView().renderPassengerType(
                        getView().getString(R.string.flightbooking_price_child_label));
            } else if (isInfantPassenger()) {
                getView().renderPassengerType(
                        getView().getString(R.string.flightbooking_price_infant_label));
            }
        }

        if (flightBookingPassengerViewModel.getPassengerBirthdate() != null) {
            getView().renderPassengerBirthdate(
                    FlightDateUtil.dateToString(
                            FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_FORMAT,
                                    flightBookingPassengerViewModel.getPassengerBirthdate()),
                            FlightDateUtil.DEFAULT_VIEW_FORMAT
                    )
            );
        }

        if (flightBookingPassengerViewModel.getPassportNumber() != null &&
                flightBookingPassengerViewModel.getPassportNumber().length() > 0) {
            getView().renderPassportNumber(flightBookingPassengerViewModel.getPassportNumber());
        }

        if (flightBookingPassengerViewModel.getPassportExpiredDate() != null &&
                flightBookingPassengerViewModel.getPassportExpiredDate().length() > 0) {
            getView().renderPassportExpiredDate(FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT,
                    FlightDateUtil.DEFAULT_VIEW_FORMAT, flightBookingPassengerViewModel.getPassportExpiredDate()));
        }

        if (flightBookingPassengerViewModel.getPassportNationality() != null) {
            getView().renderPassportNationality(flightBookingPassengerViewModel
                    .getPassportNationality().getCountryName());
        }

        if (flightBookingPassengerViewModel.getPassportIssuerCountry() != null) {
            getView().renderPassportIssuerCountry(flightBookingPassengerViewModel
                    .getPassportIssuerCountry().getCountryName());
        }

        getView().renderSelectedTitle(flightBookingPassengerViewModel.getPassengerTitle());
        getView().renderPassengerName(flightBookingPassengerViewModel.getPassengerFirstName(),
                flightBookingPassengerViewModel.getPassengerLastName());

    }

    private boolean isAdultPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == ADULT;
    }

    private boolean isChildPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == CHILDREN;
    }

    private boolean isInfantPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == INFANT;
    }

    private int getTitleId() {
        switch (getView().getPassengerTitlePosition()) {
            case 1:
                return TUAN;
            case 2:
                if (isAdultPassenger()) {
                    return NYONYA;
                } else if (isChildPassenger() || isInfantPassenger()) {
                    return NONA;
                }
            case 3:
                return NONA;
            default:
                return TUAN;
        }
    }

    private boolean validateFields() {
        boolean isValid = true;
        boolean isNeedPassport = !getView().getIsDomestic();

        Date twelveYearsAgo = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(getView().getDepartureDate()),
                Calendar.YEAR, MINUS_TWELVE
        );
        Date twoYearsAgo = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(getView().getDepartureDate()),
                Calendar.YEAR, MINUS_TWO
        );
        Date sixMonthFromDeparture = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(getView().getDepartureDate()),
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
                !flightPassengerInfoValidator.validateBirthdateNotEmpty(getView().getPassengerBirthdate())) {
            isValid = false;
            getView().showPassengerBirthdateEmptyError(R.string.flight_booking_passenger_birthdate_empty_error);
        } else if ((isAdultPassenger()) && !flightPassengerInfoValidator.validateBirthdateNotEmpty(
                getView().getPassengerBirthdate())) {
            isValid = false;
            getView().showPassengerBirthdateEmptyError(R.string.flight_booking_passenger_birthdate_empty_error);
        } else if (isAdultPassenger() && flightPassengerInfoValidator.validateBirthdateNotEmpty(
                getView().getPassengerBirthdate()) &&
                flightPassengerInfoValidator.validateDateMoreThan(getView().getPassengerBirthdate(), twelveYearsAgo)) {
            isValid = false;
            getView().showPassengerAdultBirthdateShouldMoreThan12Years(R.string.flight_booking_passenger_birthdate_adult_shoud_more_than_twelve_years);
        } else if (isChildPassenger() && flightPassengerInfoValidator.validateDateMoreThan(
                getView().getPassengerBirthdate(), twoYearsAgo)) {
            isValid = false;
            getView().showPassengerChildBirthdateShouldMoreThan2Years(R.string.flight_booking_passenger_birthdate_child_shoud_more_than_two_years);
        } else if (isInfantPassenger() && flightPassengerInfoValidator.validateDateLessThan(
                getView().getPassengerBirthdate(), twoYearsAgo)) {
            isValid = false;
            getView().showPassengerInfantBirthdateShouldNoMoreThan2Years(R.string.flight_booking_passenger_birthdate_infant_should_no_more_than_two_years);
        } else if (isNeedPassport && !flightPassengerInfoValidator.validatePassportNumberNotEmpty(getView().getPassportNumber())) {
            isValid = false;
            getView().showPassengerPassportNumberEmptyError(R.string.flight_booking_passport_number_empty_error);
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

}
