package com.tokopedia.common.travel.presentation.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.di.CommonTravelComponent;
import com.tokopedia.common.travel.domain.AddTravelPassengerUseCase;
import com.tokopedia.common.travel.domain.EditTravelPassengerUseCase;
import com.tokopedia.common.travel.presentation.activity.TravelPassengerUpdateActivity;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerUpdateContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.presentation.model.TravelTrip;
import com.tokopedia.common.travel.presentation.presenter.TravelPassengerUpdatePresenter;
import com.tokopedia.common.travel.utils.CommonTravelUtils;
import com.tokopedia.common.travel.utils.TravelDateUtil;
import com.tokopedia.common.travel.utils.typedef.TravelBookingPassenger;
import com.tokopedia.common.travel.utils.typedef.TravelPassengerTitle;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.usecase.RequestParams;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 21/06/18.
 */
public class TravelPassengerUpdateFragment extends BaseDaggerFragment
        implements TravelPassengerUpdateContract.View {

    private final String SUFFIX_BIRTHDATE = "T00:00:00Z";

    private SpinnerTextView spTitle;
    private AppCompatEditText firstNameEt;
    private AppCompatEditText lastNameEt;
    private AppCompatEditText idNumberEt;
    private AppCompatButton submitBtn;
    private AppCompatEditText birthDateEt;
    private TravelPassenger trainPassengerViewModel;
    private ActionListener listener;
    private LinearLayout formPassengerLinear;
    private String birthdate;
    private int typePage;
    private TravelTrip travelTrip;

    private int year, month, dayOfMonth;

    @Inject
    TravelPassengerUpdatePresenter presenter;

    public static Fragment newInstance(TravelTrip travelTrip,
                                       int typePage) {
        Fragment fragment = new TravelPassengerUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TravelPassengerUpdateActivity.TRAVEL_TRIP, travelTrip);
        bundle.putInt(TravelPassengerUpdateActivity.TYPE_PASSENGER_PAGE, typePage);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_passenger_booking, container, false);
        spTitle = view.findViewById(R.id.sp_title);
        firstNameEt = view.findViewById(R.id.et_first_name);
        lastNameEt = view.findViewById(R.id.et_last_name);
        idNumberEt = view.findViewById(R.id.et_identity_number);
        birthDateEt = view.findViewById(R.id.et_birth_date);
        submitBtn = view.findViewById(R.id.button_submit);
        formPassengerLinear = view.findViewById(R.id.form_passenger_layout);

        Calendar calendar = TravelDateUtil.getCurrentCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        typePage = getArguments().getInt(TravelPassengerUpdateActivity.TYPE_PASSENGER_PAGE, 0);
        travelTrip = getArguments().getParcelable(TravelPassengerUpdateActivity.TRAVEL_TRIP);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardHandler.hideSoftKeyboard(getActivity());
                if (typePage == TravelPassengerUpdateActivity.ADD_PASSENGER_TYPE) {
                    presenter.submitAddPassengerData();
                } else if (typePage == TravelPassengerUpdateActivity.EDIT_PASSENGER_TYPE) {
                    presenter.submitEditPassengerData(trainPassengerViewModel.getIdPassenger());
                }
            }
        });

        birthDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        presenter.onChangeBirthdate(year, month, dayOfMonth);
                    }
                }, year, month, dayOfMonth);
                DatePicker datePicker = datePickerDialog.getDatePicker();
                calendar.add(Calendar.DATE, -1);
                datePicker.setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        return view;
    }

    @Override
    public Date getUpperBirthDate() {
        return TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, travelTrip.getUpperBirthDate());
    }

    @Override
    public Date getLowerBirthDate() {
        return TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, travelTrip.getLowerBirthDate());
    }

    @Override
    public void showBirthdateChange(Date dateSelected) {
        birthdate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, dateSelected);
        String dateString = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, dateSelected);
        birthDateEt.setText(dateString);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (travelTrip.getTravelPassengerBooking().getPaxType() == TravelBookingPassenger.ADULT) {
            renderSpinnerForAdult();
        } else if (travelTrip.getTravelPassengerBooking().getPaxType() == TravelBookingPassenger.INFANT) {
            renderSpinnerForInfant();
        }
        renderPassengerData();
        KeyboardHandler.DropKeyboard(getActivity(), formPassengerLinear);
    }

    private void renderSpinnerForAdult() {
        String[] entries = getResources().getStringArray(R.array.travel_adult_spinner_titles);
        spTitle.setEntries(entries);
        spTitle.setValues(entries);
    }

    private void renderSpinnerForInfant() {
        String[] entries = getResources().getStringArray(R.array.travel_child_infant_spinner_titles);
        spTitle.setEntries(entries);
        spTitle.setValues(entries);
    }

    private void renderPassengerData() {
        trainPassengerViewModel = travelTrip.getTravelPassengerBooking();
        spTitle.setHint("");
        if (trainPassengerViewModel.getTitle() > 0) {
            spTitle.setSpinnerPosition(trainPassengerViewModel.getTitle() - 1);
        } else {
            spTitle.setSpinnerValue(null);
        }
        if (!TextUtils.isEmpty(trainPassengerViewModel.getFirstName()))
            firstNameEt.setText(trainPassengerViewModel.getFirstName());
        if (!TextUtils.isEmpty(trainPassengerViewModel.getLastName()))
            lastNameEt.setText(trainPassengerViewModel.getLastName());
        if (!TextUtils.isEmpty(trainPassengerViewModel.getIdNumber()))
            idNumberEt.setText(trainPassengerViewModel.getIdNumber());
        if (!TextUtils.isEmpty(trainPassengerViewModel.getBirthDate())) {
            birthDateEt.setText(getBirthDateStringPassenger());
        }
    }

    private String getBirthDateStringPassenger() {
        Date date = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                trainPassengerViewModel.getBirthDate());
        birthdate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, date);
        String birthdateString = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, date);
        Calendar calendar = TravelDateUtil.getCurrentCalendar();
        calendar.setTime(date);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        return year > 1 ? birthdateString : "";
    }

    @Override
    public int getPaxType() {
        return trainPassengerViewModel.getPaxType();
    }

    @Override
    public String getSalutationTitle() {
        return spTitle.getSpinnerValue().equalsIgnoreCase(String.valueOf(SpinnerTextView.DEFAULT_INDEX_NOT_SELECTED))
                ? "" : spTitle.getSpinnerValue();
    }

    @Override
    public String getFirstName() {
        return firstNameEt.getText().toString().trim();
    }

    @Override
    public String getLastName() {
        return lastNameEt.getText().toString().trim();
    }

    @Override
    public String getIdentityNumber() {
        return idNumberEt.getText().toString().trim();
    }

    @Override
    public String getBirthdate() {
        return birthDateEt.getText().toString();
    }

    @Override
    public int getSpinnerPosition() {
        return spTitle.getSpinnerPosition();
    }

    @Override
    public int getTravelPlatformType() {
        return travelTrip.getTravelPlatformType();
    }

    @Override
    public RequestParams getRequestParamAddPassenger() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(AddTravelPassengerUseCase.NAME_USER,
                String.format("%1$s %2$s", getFirstName(), getLastName()));
        requestParams.putString(AddTravelPassengerUseCase.FIRSTNAME, getFirstName());
        requestParams.putString(AddTravelPassengerUseCase.LASTNAME, getLastName());
        requestParams.putString(AddTravelPassengerUseCase.ID_NUMBER, getIdentityNumber());
        requestParams.putInt(AddTravelPassengerUseCase.TITLE, getSalutationId());
        requestParams.putInt(AddTravelPassengerUseCase.PAX_TYPE, trainPassengerViewModel.getPaxType());
        if (birthdate != null) {
            requestParams.putString(AddTravelPassengerUseCase.BIRTHDATE, birthdate + SUFFIX_BIRTHDATE);
        }
        return requestParams;
    }

    @Override
    public RequestParams getRequestParamEditPassenger() {
        RequestParams requestParams = getRequestParamAddPassenger();
        requestParams.putString(EditTravelPassengerUseCase.ID_PASSENGER, trainPassengerViewModel.getId());
        requestParams.putInt(EditTravelPassengerUseCase.TRAVEL_ID, trainPassengerViewModel.getTravelId());
        return requestParams;
    }

    private int getSalutationId() {
        switch (getSpinnerPosition()) {
            case 0:
                return TravelPassengerTitle.TUAN;
            case 1:
                if (getPaxType() == TravelBookingPassenger.INFANT) {
                    return TravelPassengerTitle.NONA;
                } else {
                    return TravelPassengerTitle.NYONYA;
                }
            case 2:
                return TravelPassengerTitle.NONA;
            default:
                return 0;
        }
    }

    @SuppressWarnings("Range")
    @Override
    public void showMessageErrorInSnackBar(int resId) {
        ToasterError.showClose(getActivity(), getString(resId));
    }

    @Override
    public void showMessageErrorInSnackBar(Throwable throwable) {
        ToasterError.showClose(getActivity(), ErrorHandler.getErrorMessage(getActivity(), throwable));
    }

    @Override
    public void navigateToPassengerList() {
        listener.navigateToPassengerList();
    }

    @Override
    protected void initInjector() {
        GraphqlClient.init(getActivity());
        CommonTravelComponent commonTravelComponent = CommonTravelUtils.getTrainComponent(getActivity().getApplication());
        commonTravelComponent.inject(this);
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        listener = (ActionListener) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    public interface ActionListener {
        void navigateToPassengerList();
    }
}
