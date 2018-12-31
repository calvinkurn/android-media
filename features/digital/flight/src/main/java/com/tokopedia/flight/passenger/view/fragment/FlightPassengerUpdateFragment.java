package com.tokopedia.flight.passenger.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.activity.FlightBookingNationalityActivity;
import com.tokopedia.flight.booking.view.fragment.FlightBookingNationalityFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.passenger.di.FlightPassengerComponent;
import com.tokopedia.flight.passenger.view.presenter.FlightPassengerUpdateContract;
import com.tokopedia.flight.passenger.view.presenter.FlightPassengerUpdatePresenter;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * @author by furqan on 12/03/18.
 */

public class FlightPassengerUpdateFragment extends BaseDaggerFragment implements FlightPassengerUpdateContract.View {

    public static final String EXTRA_PASSENGER_VIEW_MODEL = "EXTRA_PASSENGER_VIEW_MODEL";
    public static final String EXTRA_DEPARTURE_DATE = "EXTRA_DEPARTURE_DATE";
    public static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    public static final String EXTRA_IS_DOMESTIC = "EXTRA_IS_DOMESTIC";
    private static final int REQUEST_CODE_PICK_NATIONALITY = 4;
    private static final int REQUEST_CODE_PICK_ISSUER_COUNTRY = 5;

    @Inject
    FlightPassengerUpdatePresenter presenter;
    private FlightBookingPassengerViewModel flightBookingPassengerViewModel;

    private SpinnerTextView spPassengerTitle;
    private AppCompatEditText etPassengerType;
    private AppCompatEditText etPassengerFirstName;
    private AppCompatEditText etPassengerLastName;
    private AppCompatEditText etPassengerBirthdate;
    private AppCompatEditText etPassportNumber;
    private AppCompatEditText etPassportExpired;
    private AppCompatEditText etPassportNationality;
    private AppCompatEditText etPassportIssuerCountry;
    private AppCompatButton btnSavePassengerInfo;
    private LinearLayout passportContainer;

    public FlightPassengerUpdateFragment() {
    }

    public static FlightPassengerUpdateFragment newInstance(FlightBookingPassengerViewModel passengerViewModel,
                                                            String departureDate, String requestId, boolean isDomestic) {
        FlightPassengerUpdateFragment flightPassengerUpdateFragment = new FlightPassengerUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PASSENGER_VIEW_MODEL, passengerViewModel);
        bundle.putString(EXTRA_DEPARTURE_DATE, departureDate);
        bundle.putString(EXTRA_REQUEST_ID, requestId);
        bundle.putBoolean(EXTRA_IS_DOMESTIC, isDomestic);
        flightPassengerUpdateFragment.setArguments(bundle);
        return flightPassengerUpdateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_passenger_update, container, false);
        spPassengerTitle = view.findViewById(R.id.sp_title);
        etPassengerType = view.findViewById(R.id.et_passenger_type);
        etPassengerFirstName = view.findViewById(R.id.et_first_name);
        etPassengerLastName = view.findViewById(R.id.et_last_name);
        etPassengerBirthdate = view.findViewById(R.id.et_birth_date);
        etPassportNumber = view.findViewById(R.id.et_passport_no);
        etPassportExpired = view.findViewById(R.id.et_passport_expiration_date);
        etPassportNationality = view.findViewById(R.id.et_nationality);
        etPassportIssuerCountry = view.findViewById(R.id.et_passport_issuer_country);
        passportContainer = view.findViewById(R.id.container_passport_data);
        btnSavePassengerInfo = view.findViewById(R.id.button_submit);

        etPassportExpired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPassportExpiredClicked();
            }
        });
        etPassportNationality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToChooseNationality();
            }
        });
        etPassportIssuerCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToChooseIssuerCountry();
            }
        });
        etPassengerBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBirthdateClicked();
            }
        });
        btnSavePassengerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSaveButtonClicked();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flightBookingPassengerViewModel = getArguments().getParcelable(EXTRA_PASSENGER_VIEW_MODEL);

        presenter.attachView(this);
        presenter.onViewCreated();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightPassengerComponent.class).inject(this);
    }

    @Override
    public String getDepartureDate() {
        if (getArguments() != null) {
            return getArguments().getString(EXTRA_DEPARTURE_DATE);
        }
        return "";
    }

    @Override
    public FlightBookingPassengerViewModel getCurrentPassengerViewModel() {
        return flightBookingPassengerViewModel;
    }

    @Override
    public String getPassengerBirthdate() {
        return etPassengerBirthdate.getText().toString().trim();
    }

    @Override
    public String getPassengerFirstName() {
        return etPassengerFirstName.getText().toString().trim();
    }

    @Override
    public String getPassengerLastName() {
        return etPassengerLastName.getText().toString().trim();
    }

    @Override
    public String getPassengerTitle() {
        return spPassengerTitle.getSpinnerValue().equalsIgnoreCase(
                getString(R.string.flight_passenger_choose_salutation)) ? "" :
                spPassengerTitle.getSpinnerValue();
    }

    @Override
    public int getPassengerTitlePosition() {
        return spPassengerTitle.getSpinnerPosition();
    }

    @Override
    public void setPassengerViewModel(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        this.flightBookingPassengerViewModel = flightBookingPassengerViewModel;
    }

    @Override
    public void renderSpinnerForAdult() {
        String[] entries = getResources().getStringArray(R.array.flight_adult_spinner_titles);
        spPassengerTitle.setEntries(entries);
        spPassengerTitle.setValues(entries);
    }

    @Override
    public void renderSpinnerForChildAndInfant() {
        String[] entries = getResources().getStringArray(R.array.flight_child_infant_spinner_titles);
        spPassengerTitle.setEntries(entries);
        spPassengerTitle.setValues(entries);
    }

    @Override
    public void renderSelectedTitle(String passengerTitle) {
        spPassengerTitle.setSpinnerValueByEntries(passengerTitle);
    }

    @Override
    public void renderPassengerType(String typeText) {
        etPassengerType.setText(typeText);
    }

    @Override
    public void renderPassengerName(String firstName, String lastName) {
        etPassengerFirstName.setText(firstName);
        etPassengerLastName.setText(lastName);
    }

    @Override
    public void renderPassengerBirthdate(String birthdate) {
        etPassengerBirthdate.setText(birthdate);
    }

    @Override
    public void showBirthdatePickerDialog(Date selectedDate, final Date minDate, final Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.onBirthdateChanged(year, month, dayOfMonth, minDate, maxDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(minDate.getTime());
        datePicker.setMaxDate(maxDate.getTime());
        datePickerDialog.show();
    }

    @Override
    public void showBirthdatePickerDialog(Date selectedDate, final Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.onBirthdateChanged(year, month, dayOfMonth, maxDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(maxDate.getTime());
        datePickerDialog.show();
    }

    @Override
    public void showPassengerChildBirthdateShouldMoreThan2Years(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerInfantBirthdateShouldNoMoreThan2Years(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerAdultBirthdateShouldMoreThan12Years(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerNameEmptyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerFirstNameShouldAlphabetAndSpaceOnlyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerFirstNameShouldNoMoreThanMaxError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerLastNameShouldSameWithFirstNameError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerLastNameEmptyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerLastNameShouldOneWordError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerLastNameShouldAlphabetAndSpaceOnlyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerTitleEmptyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerBirthdateEmptyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void onSuccessUpdatePassengerData() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onErrorUpdatePassengerData() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    @Override
    public String getRequestId() {
        return getArguments().getString(EXTRA_REQUEST_ID);
    }

    @Override
    public void hidePassportContainer() {
        passportContainer.setVisibility(View.GONE);
    }

    @Override
    public void showPassportContainer() {
        passportContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderPassportNumber(String passportNumber) {
        etPassportNumber.setText(passportNumber);
    }

    @Override
    public void renderPassportExpiredDate(String passportExpiredDate) {
        etPassportExpired.setText(passportExpiredDate);
    }

    @Override
    public void renderPassportNationality(String passportNationality) {
        etPassportNationality.setText(passportNationality);
    }

    @Override
    public void renderPassportIssuerCountry(String passportIssuerCountry) {
        etPassportIssuerCountry.setText(passportIssuerCountry);
    }

    @Override
    public boolean getIsDomestic() {
        return getArguments().getBoolean(EXTRA_IS_DOMESTIC);
    }

    @Override
    public String getPassportNumber() {
        return etPassportNumber.getText().toString().trim();
    }

    @Override
    public String getPassportExpiredDate() {
        return etPassportExpired.getText().toString().trim();
    }

    @Override
    public void showPassengerPassportNumberEmptyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerPassportExpiredDateEmptyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassportNationalityEmptyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassportIssuerCountryEmptyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassportExpiredDateShouldMoreThan6MonthsFromDeparture(int resId, String dateAfterSixMonth) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(),
                String.format(getString(resId), dateAfterSixMonth));
    }

    @Override
    public void showPassportExpiredDateMax20Years(int resId, String dateAfterTwentyYears) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(),
                String.format(getString(resId), dateAfterTwentyYears));
    }

    @Override
    public void showPassportExpiredDatePickerDialog(Date selectedDate, final Date minDate, final Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.onPassportExpiredDateChanged(year, month, dayOfMonth, minDate, maxDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker1 = datePicker.getDatePicker();
        datePicker1.setMinDate(minDate.getTime());
        datePicker1.setMaxDate(maxDate.getTime());
        datePicker.show();
    }

    @Override
    public void hideKeyboard() {
        KeyboardHandler.hideSoftKeyboard(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        clearAllKeyboardFocus();
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PICK_NATIONALITY:
                    if (data != null) {
                        FlightBookingPhoneCodeViewModel flightPassportNationalityViewModel = data.getParcelableExtra(FlightBookingNationalityFragment.EXTRA_SELECTED_COUNTRY);
                        presenter.onNationalityChanged(flightPassportNationalityViewModel);
                    }
                    break;
                case REQUEST_CODE_PICK_ISSUER_COUNTRY:
                    if (data != null) {
                        FlightBookingPhoneCodeViewModel flightPassportIssuerCountry = data.getParcelableExtra(FlightBookingNationalityFragment.EXTRA_SELECTED_COUNTRY);
                        presenter.onIssuerCountryChanged(flightPassportIssuerCountry);
                    }
            }
        }
    }

    private void clearAllKeyboardFocus() {
        KeyboardHandler.hideSoftKeyboard(getActivity());
    }

    private void navigateToChooseNationality() {
        startActivityForResult(FlightBookingNationalityActivity.createIntent(getContext(), getString(R.string.flight_nationality_search_hint)), REQUEST_CODE_PICK_NATIONALITY);
    }

    private void navigateToChooseIssuerCountry() {
        startActivityForResult(FlightBookingNationalityActivity.createIntent(getContext(), getString(R.string.flight_passport_search_hint)), REQUEST_CODE_PICK_ISSUER_COUNTRY);
    }

    private void showMessageErrorInSnackbar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }
}
