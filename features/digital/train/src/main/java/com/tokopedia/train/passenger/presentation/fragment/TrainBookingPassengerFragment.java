package com.tokopedia.train.passenger.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.common.util.TrainFlowExtraConstant;
import com.tokopedia.train.common.util.TrainFlowUtil;
import com.tokopedia.train.passenger.di.DaggerTrainBookingPassengerComponent;
import com.tokopedia.train.passenger.domain.TrainSoftBookingUseCase;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.passenger.domain.requestmodel.TrainBuyerRequest;
import com.tokopedia.train.passenger.domain.requestmodel.TrainPassengerRequest;
import com.tokopedia.train.passenger.domain.requestmodel.TrainScheduleRequest;
import com.tokopedia.train.passenger.presentation.activity.TrainBookingAddPassengerActivity;
import com.tokopedia.train.passenger.presentation.activity.TrainBookingPassengerActivity;
import com.tokopedia.train.passenger.presentation.adapter.TrainBookingPassengerAdapter;
import com.tokopedia.train.passenger.presentation.adapter.TrainBookingPassengerAdapterListener;
import com.tokopedia.train.passenger.presentation.adapter.TrainBookingPassengerAdapterTypeFactory;
import com.tokopedia.train.passenger.presentation.adapter.TrainFullDividerItemDecoration;
import com.tokopedia.train.passenger.presentation.contract.TrainBookingPassengerContract;
import com.tokopedia.train.passenger.presentation.presenter.TrainBookingPassengerPresenter;
import com.tokopedia.train.passenger.presentation.viewmodel.ProfileBuyerInfo;
import com.tokopedia.train.passenger.presentation.viewmodel.TrainParamPassenger;
import com.tokopedia.train.passenger.presentation.viewmodel.TrainPassengerViewModel;
import com.tokopedia.train.reviewdetail.presentation.activity.TrainReviewDetailActivity;
import com.tokopedia.train.scheduledetail.presentation.activity.TrainScheduleDetailActivity;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.train.seat.presentation.activity.TrainSeatActivity;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 21/06/18.
 */
public class TrainBookingPassengerFragment extends BaseDaggerFragment implements TrainBookingPassengerContract.View {

    public static final int ADD_PASSENGER_REQUEST_CODE = 1009;
    public static final int NEXT_STEP_REQUEST_CODE = 1010;
    private static final String TRAIN_PARAM_PASSENGER = "train_param_passenger";

    private CardWithAction cardActionDeparture;
    private CardWithAction cardActionReturn;
    private TrainScheduleBookingPassData trainScheduleBookingPassData;
    private TrainBookingPassengerAdapter adapter;
    private RecyclerView recyclerViewPassenger;
    private TrainParamPassenger trainParamPassenger;
    private AppCompatEditText contactNameBuyer;
    private AppCompatEditText phoneNumberBuyer;
    private AppCompatEditText emailBuyer;
    private TkpdHintTextInputLayout tilContactNameBuyer;
    private TkpdHintTextInputLayout tilPhoneNumberBuyer;
    private TkpdHintTextInputLayout tilEmailBuyer;
    private AppCompatButton submitButton, chooseSeatButton;
    private AppCompatCheckBox sameAsBuyerCheckbox;
    private TrainPassengerViewModel buyerViewModel;
    private List<TrainPassengerRequest> trainPassengerRequestList;
    private TrainBuyerRequest trainBuyerRequest;
    private TrainScheduleRequest departureTripRequest;
    private TrainScheduleRequest returnTripRequest;

    @Inject
    TrainFlowUtil trainFlowUtil;

    @Inject
    TrainBookingPassengerPresenter presenter;

    public static Fragment newInstance(TrainScheduleBookingPassData trainScheduleBookingPassData) {
        TrainBookingPassengerFragment fragment = new TrainBookingPassengerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TrainBookingPassengerActivity.TRAIN_SCHEDULE_BOOKING, trainScheduleBookingPassData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_booking_passenger, container, false);
        cardActionDeparture = view.findViewById(R.id.train_departure_info);
        cardActionReturn = view.findViewById(R.id.train_return_info);
        recyclerViewPassenger = view.findViewById(R.id.rv_passengers);
        recyclerViewPassenger.addItemDecoration(new TrainFullDividerItemDecoration(recyclerViewPassenger.getContext()));
        contactNameBuyer = view.findViewById(R.id.et_contact_name);
        phoneNumberBuyer = view.findViewById(R.id.et_phone_number);
        tilEmailBuyer = view.findViewById(R.id.til_email);
        tilContactNameBuyer = view.findViewById(R.id.til_contact_name);
        tilPhoneNumberBuyer = view.findViewById(R.id.til_phone_number);
        emailBuyer = view.findViewById(R.id.et_email);
        submitButton = view.findViewById(R.id.button_submit);
        chooseSeatButton = view.findViewById(R.id.button_choose_seat);
        sameAsBuyerCheckbox = view.findViewById(R.id.checkbox);
        trainBuyerRequest = new TrainBuyerRequest();
        trainPassengerRequestList = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeBuyerInfo();
        initializeTripInfo();
        initializeCheckboxSameAsBuyer();
        initializePassengerLayout();
        initializeActionButton();

        if (savedInstanceState == null) {
            trainParamPassenger = new TrainParamPassenger();
            initializedDataPassenger();
            trainParamPassenger.setCheckedSameAsBuyer(true);
        } else {
            trainParamPassenger = savedInstanceState.getParcelable(TRAIN_PARAM_PASSENGER);
            renderPassengers(trainParamPassenger.getTrainPassengerViewModelList());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TRAIN_PARAM_PASSENGER, trainParamPassenger);
    }

    private void initializeCheckboxSameAsBuyer() {
        sameAsBuyerCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (trainParamPassenger.isCheckedSameAsBuyer()) {
                        presenter.wrapPassengerSameAsBuyer();
                    }
                } else {
                    if (!TextUtils.isEmpty(getCurrentPassengerList().get(0).getName())) {
                        adapter.clearElement(buyerViewModel);
                        presenter.removePassengerSameAsBuyer();
                    }
                    trainParamPassenger.setCheckedSameAsBuyer(true);
                }
                setEnableViewBuyerInfo();
            }
        });
    }

    private void initializeActionButton() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSubmitButtonClicked();
            }
        });

        chooseSeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onChooseSeatButtonClicked();
            }
        });
    }

    private void initializeBuyerInfo() {
        presenter.getProfilBuyer();
    }

    private void initializeTripInfo() {
        trainScheduleBookingPassData = getArguments().getParcelable(TrainBookingPassengerActivity.TRAIN_SCHEDULE_BOOKING);
        presenter.getDetailSchedule(trainScheduleBookingPassData.getDepartureScheduleId(), cardActionDeparture);
        presenter.getDetailSchedule(trainScheduleBookingPassData.getReturnScheduleId(), cardActionReturn);

        cardActionDeparture.setActionListener(() -> {
            Intent intent = TrainScheduleDetailActivity.createIntent(getActivity(),
                    trainScheduleBookingPassData.getDepartureScheduleId(),
                    trainScheduleBookingPassData.getAdultPassenger(),
                    trainScheduleBookingPassData.getInfantPassenger(),
                    false);
            startActivity(intent);
        });

        cardActionReturn.setActionListener(() -> {
            Intent intent = TrainScheduleDetailActivity.createIntent(getActivity(),
                    trainScheduleBookingPassData.getReturnScheduleId(),
                    trainScheduleBookingPassData.getAdultPassenger(),
                    trainScheduleBookingPassData.getInfantPassenger(),
                    false);
            startActivity(intent);
        });
    }

    private void initializePassengerLayout() {
        TrainBookingPassengerAdapterTypeFactory adapterTypeFactory = new TrainBookingPassengerAdapterTypeFactory(new TrainBookingPassengerAdapterListener() {
            @Override
            public void onChangePassengerData(TrainPassengerViewModel trainPassengerViewModel) {
                startActivityForResult(TrainBookingAddPassengerActivity.callingIntent(getActivity(), trainPassengerViewModel, false), ADD_PASSENGER_REQUEST_CODE);
            }
        });
        adapter = new TrainBookingPassengerAdapter(adapterTypeFactory, new ArrayList<Visitable>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewPassenger.setLayoutManager(linearLayoutManager);
        recyclerViewPassenger.setHasFixedSize(true);
        recyclerViewPassenger.setNestedScrollingEnabled(false);
        recyclerViewPassenger.setAdapter(adapter);
    }

    private void initializedDataPassenger() {
        presenter.processInitPassengers(trainScheduleBookingPassData.getAdultPassenger(),
                trainScheduleBookingPassData.getInfantPassenger());
    }

    @Override
    public void renderPassengers(List<TrainPassengerViewModel> trainPassengerViewModels) {
        adapter.clearAllElements();
        adapter.addElement(trainPassengerViewModels);
    }

    @Override
    public void setCurrentListPassenger(List<TrainPassengerViewModel> trainPassengerViewModels) {
        trainParamPassenger.setTrainPassengerViewModelList(trainPassengerViewModels);

        this.trainPassengerRequestList.clear();
        for (int i = 0; i < trainPassengerViewModels.size(); i++) {
            TrainPassengerRequest trainPassengerRequest = new TrainPassengerRequest();
            trainPassengerRequest.setIdNumber(trainPassengerViewModels.get(i).getIdentityNumber());
            trainPassengerRequest.setName(trainPassengerViewModels.get(i).getName());
            trainPassengerRequest.setPaxType(trainPassengerViewModels.get(i).getPaxType());
            trainPassengerRequest.setPhoneNumber(trainPassengerViewModels.get(i).getPhone());
            trainPassengerRequest.setSalutationId(trainPassengerViewModels.get(i).getSalutationId());
            this.trainPassengerRequestList.add(trainPassengerRequest);
        }
    }

    @Override
    public List<TrainPassengerViewModel> getCurrentPassengerList() {
        return trainParamPassenger.getTrainPassengerViewModelList();
    }

    @Override
    public void loadDetailSchedule(TrainScheduleViewModel trainScheduleViewModel, CardWithAction cardWithAction) {
        cardWithAction.setContentInfo(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.DEFAULT_VIEW_FORMAT, trainScheduleViewModel.getDepartureTimestamp()));
        cardWithAction.setSubContent(trainScheduleViewModel.getTrainName() + " " + trainScheduleViewModel.getTrainNumber());
        String timeDepartureString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getDepartureTimestamp());
        String timeArrivalString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getArrivalTimestamp());
        String departureHour = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_DAY, trainScheduleViewModel.getDepartureTimestamp());
        String arrivalHour = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_DAY, trainScheduleViewModel.getArrivalTimestamp());
        int deviationDay = Integer.parseInt(arrivalHour) - Integer.parseInt(departureHour);
        String deviationDayString = deviationDay > 0 ? " (+" + deviationDay + "h)" : "";
        cardWithAction.setSubContentInfo(" | " + timeDepartureString + " - " + timeArrivalString + deviationDayString);
    }

    @Override
    public void hideDetailSchedule() {
        hideReturnTripInfo();
        hideDepartureTripInfo();
    }

    @Override
    public void showReturnTripInfo() {
        cardActionReturn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideReturnTripInfo() {
        cardActionReturn.setVisibility(View.GONE);
    }

    @Override
    public void hideDepartureTripInfo() {
        cardActionDeparture.setVisibility(View.GONE);
    }

    @Override
    public void showDepartureTripInfo() {
        cardActionDeparture.setVisibility(View.VISIBLE);
    }

    @Override
    public String getOriginCity() {
        return trainScheduleBookingPassData.getOriginCity();
    }

    @Override
    public String getDestinationCity() {
        return trainScheduleBookingPassData.getDestinationCity();
    }

    @Override
    public void setCityRouteTripInfo(CardWithAction cardWithAction, String originCity, String destinationCity) {
        cardWithAction.setContent(originCity + " - " + destinationCity);
    }

    @Override
    public Observable<ProfileBuyerInfo> getObservableProfileBuyerInfo() {
        if (getActivity().getApplication() instanceof TrainRouter
                && ((TrainRouter) getActivity().getApplication())
                .getProfileInfo() != null) {
            return ((TrainRouter) getActivity().getApplication())
                    .getProfileInfo();
        }
        return Observable.empty();
    }

    @Override
    public void setContactName(String contactName) {
        contactNameBuyer.setText(contactName);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        phoneNumberBuyer.setText(phoneNumber);
    }

    @Override
    public void setEmail(String email) {
        emailBuyer.setText(email);
    }

    @Override
    public void navigateToChooseSeat(TrainSoftbook trainSoftbook) {
        startActivityForResult(TrainSeatActivity.getCallingIntent(getActivity(), TrainSoftbook.dummy(), trainScheduleBookingPassData, true), NEXT_STEP_REQUEST_CODE);
    }

    @Override
    public void navigateToReview(TrainSoftbook trainSoftbook) {
        startActivityForResult(TrainReviewDetailActivity.createIntent(getActivity(), trainSoftbook, trainScheduleBookingPassData), NEXT_STEP_REQUEST_CODE);
    }

    @Override
    public void loadPassengerSameAsBuyer(TrainPassengerViewModel trainPassengerViewModel) {
        buyerViewModel = trainPassengerViewModel;
        trainParamPassenger.setCheckedSameAsBuyer(false);
        sameAsBuyerCheckbox.setChecked(false);
        startActivityForResult(TrainBookingAddPassengerActivity.callingIntent(getActivity(), trainPassengerViewModel, true), ADD_PASSENGER_REQUEST_CODE);
    }

    @SuppressWarnings("Range")
    @Override
    public void showMessageErrorInSnackBar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    private void setEnableViewBuyerInfo() {
        tilContactNameBuyer.setEnabled(!sameAsBuyerCheckbox.isChecked());
        tilPhoneNumberBuyer.setEnabled(!sameAsBuyerCheckbox.isChecked());
        tilEmailBuyer.setEnabled(!sameAsBuyerCheckbox.isChecked());

        contactNameBuyer.setTextColor(ContextCompat.getColor(getActivity(), getColorEnableEditText()));
        phoneNumberBuyer.setTextColor(ContextCompat.getColor(getActivity(), getColorEnableEditText()));
        emailBuyer.setTextColor(ContextCompat.getColor(getActivity(), getColorEnableEditText()));
    }

    private int getColorEnableEditText() {
        return sameAsBuyerCheckbox.isChecked() ? R.color.font_black_disabled_38 : R.color.font_black_secondary_54;
    }

    @Override
    public String getContactNameEt() {
        return contactNameBuyer.getText().toString().trim();
    }

    @Override
    public String getPhoneNumberEt() {
        return phoneNumberBuyer.getText().toString().trim();
    }

    @Override
    public String getEmailEt() {
        return emailBuyer.getText().toString().trim();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_PASSENGER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    TrainPassengerViewModel trainPassengerViewModel = data.getParcelableExtra(TrainBookingAddPassengerActivity.PASSENGER_DATA);
                    presenter.updateDataPassengers(trainPassengerViewModel);

                    if (!trainParamPassenger.isCheckedSameAsBuyer() && trainPassengerViewModel.getPassengerId() == 1) {
                        sameAsBuyerCheckbox.setChecked(true);
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    trainParamPassenger.setCheckedSameAsBuyer(true);
                }
                break;
            case NEXT_STEP_REQUEST_CODE:
                if (data != null) {
                    if (data.getIntExtra(TrainFlowExtraConstant.EXTRA_FLOW_DATA, -1) != -1) {
                        trainFlowUtil.actionSetResultAndClose(
                                getActivity(),
                                getActivity().getIntent(),
                                data.getIntExtra(TrainFlowExtraConstant.EXTRA_FLOW_DATA, 0)
                        );
                    }
                }
        }
    }

    @Override
    public RequestParams getTrainSoftBookingRequestParam() {
        trainBuyerRequest.setName(getContactNameEt());
        trainBuyerRequest.setPhone(getPhoneNumberEt());
        trainBuyerRequest.setEmail(getEmailEt());

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(TrainSoftBookingUseCase.DEPARTURE_TRIP, departureTripRequest);
        requestParams.putObject(TrainSoftBookingUseCase.BUYER, trainBuyerRequest);
        requestParams.putInt(TrainSoftBookingUseCase.TOTAL_ADULT, trainScheduleBookingPassData.getAdultPassenger());
        requestParams.putInt(TrainSoftBookingUseCase.TOTAL_INFANT, trainScheduleBookingPassData.getInfantPassenger());
        requestParams.putObject(TrainSoftBookingUseCase.PASSENGERS, trainPassengerRequestList);
        requestParams.putInt(TrainSoftBookingUseCase.DEVICE, TrainSoftBookingUseCase.DEFAULT_DEVICE);
        if (returnTripRequest != null) {
            requestParams.putObject(TrainSoftBookingUseCase.RETURN_TRIP, returnTripRequest);
        }
        return requestParams;
    }

    @Override
    public void setDepartureTripRequest(TrainScheduleRequest departureTripRequest) {
        this.departureTripRequest = departureTripRequest;
    }

    @Override
    public void setReturnTripRequest(TrainScheduleRequest returnTripRequest) {
        this.returnTripRequest = returnTripRequest;
    }

    @Override
    public void showErrorSoftBooking(Throwable e) {
        String message = ErrorHandler.getErrorMessage(getActivity(), e);
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }
}
