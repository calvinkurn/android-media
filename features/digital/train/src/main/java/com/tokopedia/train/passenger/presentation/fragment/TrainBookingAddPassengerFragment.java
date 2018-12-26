package com.tokopedia.train.passenger.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.passenger.data.TrainBookingPassenger;
import com.tokopedia.train.passenger.di.DaggerTrainBookingPassengerComponent;
import com.tokopedia.train.passenger.presentation.activity.TrainBookingAddPassengerActivity;
import com.tokopedia.train.passenger.presentation.contract.TrainBookingAddPassengerContract;
import com.tokopedia.train.passenger.presentation.presenter.TrainBookingAddPassengerPresenter;
import com.tokopedia.train.passenger.presentation.viewmodel.TrainPassengerViewModel;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 21/06/18.
 */
public class TrainBookingAddPassengerFragment extends BaseDaggerFragment
        implements TrainBookingAddPassengerContract.View {

    private SpinnerTextView spTitle;
    private AppCompatEditText contactName;
    private AppCompatEditText phoneNumber;
    private AppCompatEditText identityNumber;
    private AppCompatButton submitBtn;
    private TrainPassengerViewModel trainPassengerViewModel;
    private ActionListener listener;
    private TkpdHintTextInputLayout tilContactName;
    private TkpdHintTextInputLayout tilPhoneNumber;
    private TkpdHintTextInputLayout tilIdentityNumber;
    private AppCompatTextView identityNumberInfo;
    private AppCompatTextView phoneNumberInfo;
    private boolean isCheckSameAsBuyer;

    @Inject
    TrainBookingAddPassengerPresenter presenter;

    public static Fragment newInstance(TrainPassengerViewModel trainPassengerViewModel, boolean isCheckSameAsBuyer) {
        Fragment fragment = new TrainBookingAddPassengerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TrainBookingAddPassengerActivity.PASSENGER_DATA, trainPassengerViewModel);
        bundle.putBoolean(TrainBookingAddPassengerActivity.IS_CHECK_SAME_AS_BUYER, isCheckSameAsBuyer);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_booking_add_passenger_info, container, false);
        spTitle = view.findViewById(R.id.sp_title);
        contactName = view.findViewById(R.id.et_contact_name);
        phoneNumber = view.findViewById(R.id.et_phone_number);
        identityNumber = view.findViewById(R.id.et_identity_number);
        submitBtn = view.findViewById(R.id.button_submit);
        tilContactName = view.findViewById(R.id.til_contact_name);
        tilPhoneNumber = view.findViewById(R.id.til_phone_number);
        tilIdentityNumber = view.findViewById(R.id.til_identity_number);
        identityNumberInfo = view.findViewById(R.id.identity_number_info);
        phoneNumberInfo = view.findViewById(R.id.et_phone_number_info);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardHandler.hideSoftKeyboard(getActivity());
                presenter.submitDataPassenger(trainPassengerViewModel);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trainPassengerViewModel = getArguments().getParcelable(TrainBookingAddPassengerActivity.PASSENGER_DATA);
        isCheckSameAsBuyer = getArguments().getBoolean(TrainBookingAddPassengerActivity.IS_CHECK_SAME_AS_BUYER);

        if (trainPassengerViewModel.getPaxType() == TrainBookingPassenger.ADULT) {
            renderSpinnerForAdult();
            tilPhoneNumber.setVisibility(View.VISIBLE);
            tilIdentityNumber.setVisibility(View.VISIBLE);
            identityNumberInfo.setVisibility(View.VISIBLE);
            phoneNumberInfo.setVisibility(View.VISIBLE);
        } else {
            renderSpinnerForInfant();
            tilPhoneNumber.setVisibility(View.GONE);
            tilIdentityNumber.setVisibility(View.GONE);
            identityNumberInfo.setVisibility(View.GONE);
            phoneNumberInfo.setVisibility(View.GONE);
        }
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
        if (!TextUtils.isEmpty(trainPassengerViewModel.getName()))
            contactName.setText(trainPassengerViewModel.getName());
        if (!TextUtils.isEmpty(trainPassengerViewModel.getPhone()))
            phoneNumber.setText(trainPassengerViewModel.getPhone());
        if (!TextUtils.isEmpty(trainPassengerViewModel.getIdentityNumber()))
            identityNumber.setText(trainPassengerViewModel.getIdentityNumber());

        setEnableEditTextBasedOnCheckbox();
        if (isCheckSameAsBuyer){
            identityNumber.requestFocus();
        }
    }

    private void setEnableEditTextBasedOnCheckbox() {
        tilContactName.setEnabled(!isCheckSameAsBuyer);
        tilPhoneNumber.setEnabled(!isCheckSameAsBuyer);

        contactName.setTextColor(ContextCompat.getColor(getActivity(), getColorEnableEditText()));
        phoneNumber.setTextColor(ContextCompat.getColor(getActivity(), getColorEnableEditText()));
    }

    private int getColorEnableEditText() {
        return isCheckSameAsBuyer ? R.color.font_black_disabled_38 : R.color.font_black_secondary_54;
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
    public String getContactName() {
        return contactName.getText().toString();
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber.getText().toString();
    }

    @Override
    public String getIdentityNumber() {
        return identityNumber.getText().toString();
    }

    @Override
    public int getSpinnerPosition() {
        return spTitle.getSpinnerPosition();
    }

    @Override
    public void navigateToBookingPassenger(TrainPassengerViewModel trainPassengerViewModel) {
        listener.navigateToBookingPassenger(trainPassengerViewModel);
    }

    @SuppressWarnings("Range")
    @Override
    public void showMessageErrorInSnackBar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    protected void initInjector() {
        DaggerTrainBookingPassengerComponent.builder()
                .trainComponent(TrainComponentUtils.getTrainComponent(getActivity().getApplication()))
                .build()
                .inject(this);
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
        void navigateToBookingPassenger(TrainPassengerViewModel trainPassengerViewModel);
    }
}
