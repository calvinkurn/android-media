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
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.di.CommonTravelComponent;
import com.tokopedia.common.travel.presentation.presenter.TravelPassengerUpdatePresenter;
import com.tokopedia.common.travel.presentation.activity.TravelPassengerUpdateActivity;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerUpdateContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.utils.CommonTravelUtils;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.graphql.data.GraphqlClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 21/06/18.
 */
public class TravelPassengerUpdateFragment extends BaseDaggerFragment
        implements TravelPassengerUpdateContract.View {

    public static final Locale DEFAULT_LOCALE = new Locale("in", "ID");
    public static final String DEFAULT_VIEW_FORMAT = "dd MMM yyyy";


    private SpinnerTextView spTitle;
    private AppCompatEditText firstNameEt;
    private AppCompatEditText lastNameEt;
    private AppCompatEditText idNumberEt;
    private AppCompatButton submitBtn;
    private AppCompatEditText birthDateEt;
    private TravelPassenger trainPassengerViewModel;
    private ActionListener listener;

    @Inject
    TravelPassengerUpdatePresenter presenter;

    public static Fragment newInstance(TravelPassenger trainPassengerViewModel) {
        Fragment fragment = new TravelPassengerUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TravelPassengerUpdateActivity.PASSENGER_DATA, trainPassengerViewModel);
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

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardHandler.hideSoftKeyboard(getActivity());
                presenter.submitDataPassenger(trainPassengerViewModel);
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
                        Calendar calendarNow = Calendar.getInstance();
                        calendarNow.set(Calendar.YEAR, year);
                        calendarNow.set(Calendar.MONTH, month);
                        calendarNow.set(Calendar.DATE, dayOfMonth);
                        Date dateSelected = calendar.getTime();
                        DateFormat format = new SimpleDateFormat(DEFAULT_VIEW_FORMAT, DEFAULT_LOCALE);
                        String dateString = format.format(dateSelected);
                        birthDateEt.setText(dateString);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                DatePicker datePicker = datePickerDialog.getDatePicker();

                calendar.add(Calendar.DATE, -1);
                datePicker.setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trainPassengerViewModel = getArguments().getParcelable(TravelPassengerUpdateActivity.PASSENGER_DATA);
        renderSpinnerForAdult();
        renderPassengerData();
    }

    private void renderSpinnerForAdult() {
        String[] entries = getResources().getStringArray(R.array.adult_spinner_titles);
        spTitle.setEntries(entries);
        spTitle.setValues(entries);
    }

    private void renderSpinnerForInfant() {
        String[] entries = getResources().getStringArray(R.array.child_infant_spinner_titles);
        spTitle.setEntries(entries);
        spTitle.setValues(entries);
    }

    private void renderPassengerData() {
        if (TextUtils.isEmpty(trainPassengerViewModel.getSalutationTitle())) {
            spTitle.setSpinnerPosition(0);
        } else {
            spTitle.setSpinnerValueByEntries(trainPassengerViewModel.getSalutationTitle());
        }
        if (!TextUtils.isEmpty(trainPassengerViewModel.getFirstName()))
            firstNameEt.setText(trainPassengerViewModel.getFirstName());
        if (!TextUtils.isEmpty(trainPassengerViewModel.getLastName()))
            lastNameEt.setText(trainPassengerViewModel.getLastName());
        if (!TextUtils.isEmpty(trainPassengerViewModel.getIdNumber()))
            idNumberEt.setText(trainPassengerViewModel.getIdNumber());
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
        return firstNameEt.getText().toString();
    }

    @Override
    public String getLastName() {
        return lastNameEt.getText().toString();
    }

    @Override
    public String getIdentityNumber() {
        return idNumberEt.getText().toString();
    }

    @Override
    public int getSpinnerPosition() {
        return spTitle.getSpinnerPosition();
    }

    @Override
    public void navigateToBookingPassenger(TravelPassenger trainPassengerViewModel) {
        Toast.makeText(getActivity(), "SUBMIT NEW PASSENGER", Toast.LENGTH_SHORT).show();
//        listener.navigateToBookingPassenger(trainPassengerViewModel);
    }

    @SuppressWarnings("Range")
    @Override
    public void showMessageErrorInSnackBar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
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

    public interface ActionListener {
        void navigateToBookingPassenger(TravelPassenger trainPassengerViewModel);
    }
}
