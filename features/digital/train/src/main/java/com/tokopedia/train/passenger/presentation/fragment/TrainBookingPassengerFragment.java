package com.tokopedia.train.passenger.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.common.travel.constant.TravelPlatformType;
import com.tokopedia.common.travel.presentation.activity.TravelPassengerListActivity;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.presentation.model.TravelTrip;
import com.tokopedia.common.travel.utils.typedef.TravelPassengerTitle;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.util.TrainAnalytics;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.common.util.TrainFlowConstant;
import com.tokopedia.train.common.util.TrainFlowExtraConstant;
import com.tokopedia.train.common.util.TrainFlowUtil;
import com.tokopedia.train.passenger.di.DaggerTrainBookingPassengerComponent;
import com.tokopedia.train.passenger.domain.TrainSoftBookingUseCase;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.passenger.domain.requestmodel.TrainBuyerRequest;
import com.tokopedia.train.passenger.domain.requestmodel.TrainPassengerRequest;
import com.tokopedia.train.passenger.domain.requestmodel.TrainScheduleRequest;
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
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 21/06/18.
 */
public class TrainBookingPassengerFragment extends BaseDaggerFragment implements TrainBookingPassengerContract.View {

    public static final int PASSENGER_LIST_REQUEST_CODE = 1009;
    public static final int NEXT_STEP_REQUEST_CODE = 1010;
    private static final String TRAIN_PARAM_PASSENGER = "train_param_passenger";
    private static final String TRAIN_FILL_PASSENGER_TRACE = "tr_train_fill_passenger_identity";

    private CardWithAction cardActionDeparture;
    private CardWithAction cardActionReturn;
    private TrainScheduleBookingPassData trainScheduleBookingPassData;
    private TrainBookingPassengerAdapter adapter;
    private RecyclerView recyclerViewPassenger;
    private TrainParamPassenger trainParamPassenger;
    private AppCompatEditText contactNameBuyer;
    private AppCompatEditText phoneNumberBuyer;
    private AppCompatEditText emailBuyer;
    private AppCompatButton submitButton, chooseSeatButton;
    private LinearLayout containerLayout;
    private RelativeLayout progressBar;
    private boolean resetPassengerListSelected;
    private TravelTrip travelTrip;

    private List<TrainPassengerRequest> trainPassengerRequestList;
    private TrainBuyerRequest trainBuyerRequest;
    private TrainScheduleRequest departureTripRequest;
    private TrainScheduleRequest returnTripRequest;

    private TrainScheduleViewModel departureScheduleViewModel;
    private TrainScheduleViewModel returnScheduleViewModel;
    private boolean traceStop;
    private PerformanceMonitoring performanceMonitoring;

    @Inject
    TrainAnalytics trainAnalytics;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performanceMonitoring = PerformanceMonitoring.start(TRAIN_FILL_PASSENGER_TRACE);
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
        emailBuyer = view.findViewById(R.id.et_email);
        submitButton = view.findViewById(R.id.button_submit);
        chooseSeatButton = view.findViewById(R.id.button_choose_seat);
        containerLayout = view.findViewById(R.id.container);
        progressBar = view.findViewById(R.id.progress_bar);
        trainBuyerRequest = new TrainBuyerRequest();
        travelTrip = new TravelTrip();
        trainPassengerRequestList = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeBuyerInfo();
        initializeTripInfo();
        initializePassengerLayout();
        initializeActionButton();

        if (savedInstanceState == null) {
            trainParamPassenger = new TrainParamPassenger();
            initializedDataPassenger();
        } else {
            trainParamPassenger = savedInstanceState.getParcelable(TRAIN_PARAM_PASSENGER);
            setCurrentListPassenger(trainParamPassenger.getTrainPassengerViewModelList());
            renderPassengers(trainParamPassenger.getTrainPassengerViewModelList());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TRAIN_PARAM_PASSENGER, trainParamPassenger);
    }

    private void initializeActionButton() {
        submitButton.setOnClickListener(view -> {

            int numOfTotalPassenger = trainScheduleBookingPassData.getAdultPassenger() +
                    trainScheduleBookingPassData.getInfantPassenger();

            if (returnScheduleViewModel != null) {
                trainAnalytics.eventAddToCart(
                        departureScheduleViewModel, returnScheduleViewModel, numOfTotalPassenger
                );
            } else {
                trainAnalytics.eventAddToCart(
                        departureScheduleViewModel, numOfTotalPassenger
                );
            }

            trainAnalytics.eventClickNextOnCustomersPage();

            presenter.onSubmitButtonClicked();
        });

        chooseSeatButton.setOnClickListener(view -> presenter.onChooseSeatButtonClicked());
    }

    private void initializeBuyerInfo() {
        presenter.getProfilBuyer();
    }

    private void initializeTripInfo() {
        trainScheduleBookingPassData = getArguments().getParcelable(TrainBookingPassengerActivity.TRAIN_SCHEDULE_BOOKING);
        presenter.getDetailSchedule(trainScheduleBookingPassData.getDepartureScheduleId(), cardActionDeparture);
        presenter.getDetailSchedule(trainScheduleBookingPassData.getReturnScheduleId(), cardActionReturn);

        cardActionDeparture.setActionListener(() -> {
            String origin = departureScheduleViewModel.getOrigin();
            String destination = departureScheduleViewModel.getDestination();
            String trainClass = departureScheduleViewModel.getDisplayClass();
            String trainName = departureScheduleViewModel.getTrainName();

            trainAnalytics.eventClickDetail(origin, destination, trainClass, trainName);

            Intent intent = TrainScheduleDetailActivity.createIntent(getActivity(),
                    trainScheduleBookingPassData.getDepartureScheduleId(),
                    trainScheduleBookingPassData.getAdultPassenger(),
                    trainScheduleBookingPassData.getInfantPassenger(),
                    false);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.travel_slide_up_in, R.anim.travel_anim_stay);

        });

        cardActionReturn.setActionListener(() -> {
            String origin = returnScheduleViewModel.getOrigin();
            String destination = returnScheduleViewModel.getDestination();
            String trainClass = returnScheduleViewModel.getDisplayClass();
            String trainName = returnScheduleViewModel.getTrainName();

            trainAnalytics.eventClickDetail(origin, destination, trainClass, trainName);

            Intent intent = TrainScheduleDetailActivity.createIntent(getActivity(),
                    trainScheduleBookingPassData.getReturnScheduleId(),
                    trainScheduleBookingPassData.getAdultPassenger(),
                    trainScheduleBookingPassData.getInfantPassenger(),
                    false);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.travel_slide_up_in, R.anim.travel_anim_stay);
        });
    }

    private void initializePassengerLayout() {
        TrainBookingPassengerAdapterTypeFactory adapterTypeFactory = new TrainBookingPassengerAdapterTypeFactory(new TrainBookingPassengerAdapterListener() {
            @Override
            public void onChangePassengerData(TrainPassengerViewModel trainPassengerViewModel) {
                presenter.calculateUpperLowerBirthDate(trainPassengerViewModel.getPaxType());
                travelTrip.setTravelPlatformType(TravelPlatformType.TRAIN);
                travelTrip.setTravelPassengerBooking(convertTrainPassengerViewModel(trainPassengerViewModel));
                startActivityForResult(TravelPassengerListActivity.callingIntent(getActivity(),
                        travelTrip, resetPassengerListSelected), PASSENGER_LIST_REQUEST_CODE);
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
        resetPassengerListSelected = true;
        presenter.processInitPassengers(trainScheduleBookingPassData.getAdultPassenger(),
                trainScheduleBookingPassData.getInfantPassenger());
    }

    @Override
    public void renderPassengers(List<TrainPassengerViewModel> trainPassengerViewModels) {
        adapter.clearAllElements();
        adapter.addElement(trainPassengerViewModels);
        stopTrace();
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
        cardWithAction.setContentInfo("(" + TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.DEFAULT_VIEW_FORMAT, trainScheduleViewModel.getDepartureTimestamp()) + ")");
        cardWithAction.setSubContent(trainScheduleViewModel.getTrainName() + " " + trainScheduleViewModel.getTrainNumber());
        String timeDepartureString = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getDepartureTimestamp());
        String timeArrivalString = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getArrivalTimestamp());
        String departureHour = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_DAY, trainScheduleViewModel.getDepartureTimestamp());
        String arrivalHour = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_DAY, trainScheduleViewModel.getArrivalTimestamp());
        int deviationDay = Integer.parseInt(arrivalHour) - Integer.parseInt(departureHour);
        String deviationDayString = deviationDay > 0 ? " (+" + deviationDay + "h)" : "";
        cardWithAction.setSubContentInfo(" | " + timeDepartureString + " - " + timeArrivalString + deviationDayString);

        if (!trainScheduleViewModel.isReturnTrip()) {
            this.departureScheduleViewModel = trainScheduleViewModel;
        } else {
            this.returnScheduleViewModel = trainScheduleViewModel;
        }
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
    public TrainScheduleRequest convertTripToRequestParam(TrainScheduleViewModel trainScheduleViewModel) {
        TrainScheduleRequest trainScheduleRequest = new TrainScheduleRequest();
        trainScheduleRequest.setDepartureTimestamp(trainScheduleViewModel.getDepartureTimestamp());
        trainScheduleRequest.setDestination(trainScheduleViewModel.getDestination());
        trainScheduleRequest.setOrigin(trainScheduleViewModel.getOrigin());
        trainScheduleRequest.setSubClass(trainScheduleViewModel.getSubclass());
        trainScheduleRequest.setTrainClass(trainScheduleViewModel.getClassTrain());
        trainScheduleRequest.setTrainName(trainScheduleViewModel.getTrainName());
        trainScheduleRequest.setTrainNo(trainScheduleViewModel.getTrainNumber());
        return trainScheduleRequest;
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
        startActivityForResult(TrainSeatActivity.getCallingIntent(getActivity(), trainSoftbook, trainScheduleBookingPassData, true), NEXT_STEP_REQUEST_CODE);
    }

    @Override
    public void navigateToReview(TrainSoftbook trainSoftbook) {
        startActivityForResult(TrainReviewDetailActivity.createIntent(getActivity(), trainSoftbook, trainScheduleBookingPassData), NEXT_STEP_REQUEST_CODE);
    }

    @SuppressWarnings("Range")
    @Override
    public void showMessageErrorInSnackBar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
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
            case PASSENGER_LIST_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    resetPassengerListSelected = false;
                    TravelPassenger travelPassenger = data.getParcelableExtra(TravelPassengerListActivity.PASSENGER_DATA);
                    presenter.updateDataPassengers(convertPassengerViewModel(travelPassenger));
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

    private TravelPassenger convertTrainPassengerViewModel(TrainPassengerViewModel trainPassengerViewModel) {
        TravelPassenger travelPassenger = new TravelPassenger();
        travelPassenger.setIdPassenger(trainPassengerViewModel.getIdPassenger());
        travelPassenger.setPaxType(trainPassengerViewModel.getPaxType());
        travelPassenger.setIdLocal(trainPassengerViewModel.getIdLocal());
        travelPassenger.setTitle(trainPassengerViewModel.getSalutationId());
        return travelPassenger;
    }

    private TrainPassengerViewModel convertPassengerViewModel(TravelPassenger travelPassenger) {
        TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel();
        trainPassengerViewModel.setIdLocal(travelPassenger.getIdLocal());
        trainPassengerViewModel.setIdPassenger(travelPassenger.getIdPassenger());
        trainPassengerViewModel.setName(travelPassenger.getName());
        trainPassengerViewModel.setHeaderTitle(travelPassenger.getHeaderTitle());
        trainPassengerViewModel.setIdentityNumber(travelPassenger.getIdNumber());
        trainPassengerViewModel.setPaxType(travelPassenger.getPaxType());
        trainPassengerViewModel.setSalutationId(travelPassenger.getTitle());
        trainPassengerViewModel.setSalutationTitle(getSalutationString(travelPassenger.getTitle()));
        return trainPassengerViewModel;
    }

    private String getSalutationString(int title) {
        if (title == TravelPassengerTitle.TUAN) {
            return "Tn";
        } else if (title == TravelPassengerTitle.NYONYA) {
            return "Ny";
        } else if (title == TravelPassengerTitle.NONA) {
            return "Nn";
        }
        return "";
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
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void hidePage() {
        containerLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPage() {
        containerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showNavigateToSearchDialog(String message) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.RETORIC);
        dialog.setTitle(getString(R.string.train_error_dialog_failed_booking));
        dialog.setDesc(message);
        dialog.setBtnOk(getString(R.string.train_error_dialog_default_button));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                trainFlowUtil.actionSetResultAndClose(
                        getActivity(),
                        getActivity().getIntent(),
                        TrainFlowConstant.RESEARCH
                );
            }
        });
        dialog.show();
    }

    @Override
    public Date getDepartureDate() {
        String departureDate = returnScheduleViewModel != null ? returnScheduleViewModel.getDepartureTimestamp() :
                departureScheduleViewModel.getDepartureTimestamp();
        return TrainDateUtil.stringToDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, departureDate);
    }

    @Override
    public void showUpperLowerBirthDate(String lowerBirthDate, String upperBirthDate) {
        travelTrip.setLowerBirthDate(lowerBirthDate);
        travelTrip.setUpperBirthDate(upperBirthDate);
    }

    private void stopTrace() {
        if (!traceStop) {
            performanceMonitoring.stopTrace();
            traceStop = true;
        }
    }

    @Override
    public void onDestroy() {
        presenter.onDestroyView();
        super.onDestroy();
    }
}
