package com.tokopedia.common.travel.presentation;

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
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.di.CommonTravelComponent;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.utils.CommonTravelUtils;
import com.tokopedia.common.travel.utils.typedef.TravelBookingPassenger;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.graphql.data.GraphqlClient;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 21/06/18.
 */
public class TravelPassengerBookingFragment extends BaseDaggerFragment
        implements TravelPassengerBookingContract.View {

    private SpinnerTextView spTitle;
    private AppCompatEditText contactName;
    private AppCompatEditText phoneNumber;
    private AppCompatEditText identityNumber;
    private AppCompatButton submitBtn;
    private TravelPassenger trainPassengerViewModel;
    private ActionListener listener;
    private TkpdHintTextInputLayout tilContactName;
    private TkpdHintTextInputLayout tilPhoneNumber;
    private TkpdHintTextInputLayout tilIdentityNumber;
    private AppCompatTextView identityNumberInfo;
    private boolean isCheckSameAsBuyer;

    @Inject
    TravelPassengerBookingPresenter presenter;

    public static Fragment newInstance(TravelPassenger trainPassengerViewModel, boolean isCheckSameAsBuyer) {
        Fragment fragment = new TravelPassengerBookingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TravelPassengerBookingActivity.PASSENGER_DATA, trainPassengerViewModel);
        bundle.putBoolean(TravelPassengerBookingActivity.IS_CHECK_SAME_AS_BUYER, isCheckSameAsBuyer);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_passenger_booking, container, false);
        spTitle = view.findViewById(R.id.sp_title);
        contactName = view.findViewById(R.id.et_contact_name);
        phoneNumber = view.findViewById(R.id.et_phone_number);
        identityNumber = view.findViewById(R.id.et_identity_number);
        submitBtn = view.findViewById(R.id.button_submit);
        tilContactName = view.findViewById(R.id.til_contact_name);
        tilPhoneNumber = view.findViewById(R.id.til_phone_number);
        tilIdentityNumber = view.findViewById(R.id.til_identity_number);
        identityNumberInfo = view.findViewById(R.id.identity_number_info);

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

        trainPassengerViewModel = getArguments().getParcelable(TravelPassengerBookingActivity.PASSENGER_DATA);
        isCheckSameAsBuyer = getArguments().getBoolean(TravelPassengerBookingActivity.IS_CHECK_SAME_AS_BUYER);

        if (trainPassengerViewModel.getPaxType() == TravelBookingPassenger.ADULT) {
            renderSpinnerForAdult();
            tilPhoneNumber.setVisibility(View.VISIBLE);
            tilIdentityNumber.setVisibility(View.VISIBLE);
            identityNumberInfo.setVisibility(View.VISIBLE);
        } else {
            renderSpinnerForInfant();
            tilPhoneNumber.setVisibility(View.GONE);
            tilIdentityNumber.setVisibility(View.GONE);
            identityNumberInfo.setVisibility(View.GONE);
        }
        renderPassengerData();
        presenter.getPassengerList();
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
        if (!TextUtils.isEmpty(trainPassengerViewModel.getPhoneNumber()))
            phoneNumber.setText(trainPassengerViewModel.getPhoneNumber());
        if (!TextUtils.isEmpty(trainPassengerViewModel.getIdentityNumber()))
            identityNumber.setText(trainPassengerViewModel.getIdentityNumber());

        setEnableEditTextBasedOnCheckbox();
        if (isCheckSameAsBuyer) {
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
    public void navigateToBookingPassenger(TravelPassenger trainPassengerViewModel) {
        listener.navigateToBookingPassenger(trainPassengerViewModel);
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
