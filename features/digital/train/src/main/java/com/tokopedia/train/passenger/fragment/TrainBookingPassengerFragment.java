package com.tokopedia.train.passenger.fragment;

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
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.passenger.activity.TrainBookingAddPassengerActivity;
import com.tokopedia.train.passenger.activity.TrainBookingPassengerActivity;
import com.tokopedia.train.passenger.adapter.TrainBookingPassengerAdapter;
import com.tokopedia.train.passenger.adapter.TrainBookingPassengerAdapterListener;
import com.tokopedia.train.passenger.adapter.TrainBookingPassengerAdapterTypeFactory;
import com.tokopedia.train.passenger.contract.TrainBookingPassengerContract;
import com.tokopedia.train.passenger.di.DaggerTrainBookingPassengerComponent;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.passenger.presenter.TrainBookingPassengerPresenter;
import com.tokopedia.train.passenger.viewmodel.ProfileBuyerInfo;
import com.tokopedia.train.passenger.viewmodel.TrainParamPassenger;
import com.tokopedia.train.passenger.viewmodel.TrainPassengerViewModel;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 21/06/18.
 */
public class TrainBookingPassengerFragment extends BaseDaggerFragment implements TrainBookingPassengerContract.View {

    private static final String TRAIN_PARAM_PASSENGER = "train_param_passenger";

    private CardWithAction cardActionDeparture;
    private CardWithAction cardActionReturn;
    private TrainScheduleBookingPassData trainScheduleBookingPassData;
    private TrainBookingPassengerAdapter adapter;
    private RecyclerView recyclerViewPassenger;
    private TrainParamPassenger trainParamPassenger;
    private AppCompatEditText contactNameBuyer;
    private AppCompatEditText birthdateBuyer;
    private AppCompatEditText phoneNumberBuyer;
    private AppCompatEditText emailBuyer;
    private AppCompatButton submitButton, chooseSeatButton;

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
        contactNameBuyer = view.findViewById(R.id.et_contact_name);
        birthdateBuyer = view.findViewById(R.id.et_birthdate);
        phoneNumberBuyer = view.findViewById(R.id.et_phone_number);
        emailBuyer = view.findViewById(R.id.et_contact_email);
        submitButton = view.findViewById(R.id.button_submit);
        chooseSeatButton = view.findViewById(R.id.button_choose_seat);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            trainParamPassenger = savedInstanceState.getParcelable(TRAIN_PARAM_PASSENGER);
        } else {
            trainParamPassenger = new TrainParamPassenger();
        }

        initializeBuyerInfo();
        initializeTripInfo();
        initializePassenger();
        initializeActionButton();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TRAIN_PARAM_PASSENGER, trainParamPassenger);
    }

    private void initializeTripInfo() {
        trainScheduleBookingPassData = getArguments().getParcelable(TrainBookingPassengerActivity.TRAIN_SCHEDULE_BOOKING);
        presenter.getDetailSchedule(trainScheduleBookingPassData.getDepartureScheduleId(), cardActionDeparture);
        presenter.getDetailSchedule(trainScheduleBookingPassData.getReturnScheduleId(), cardActionReturn);

        cardActionDeparture.setActionListener(new CardWithAction.ActionListener() {
            @Override
            public void actionClicked() {
                //TODO : detail info trip departure
                Toast.makeText(getActivity(), trainScheduleBookingPassData.getOriginCity() + " - " +
                        trainScheduleBookingPassData.getDestinationCity(), Toast.LENGTH_SHORT).show();
            }
        });

        cardActionReturn.setActionListener(new CardWithAction.ActionListener() {
            @Override
            public void actionClicked() {
                //TODO : detail info trip return
                Toast.makeText(getActivity(), trainScheduleBookingPassData.getDestinationCity() + " - " +
                        trainScheduleBookingPassData.getOriginCity(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializePassenger() {
        TrainBookingPassengerAdapterTypeFactory adapterTypeFactory = new TrainBookingPassengerAdapterTypeFactory(new TrainBookingPassengerAdapterListener() {
            @Override
            public void onChangePassengerData(TrainPassengerViewModel trainPassengerViewModel) {
                startActivityForResult(TrainBookingAddPassengerActivity.callingIntent(getActivity(), trainPassengerViewModel), 1);
            }
        });
        adapter = new TrainBookingPassengerAdapter(adapterTypeFactory, new ArrayList<Visitable>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewPassenger.setLayoutManager(linearLayoutManager);
        recyclerViewPassenger.setHasFixedSize(true);
        recyclerViewPassenger.setNestedScrollingEnabled(false);
        recyclerViewPassenger.setAdapter(adapter);
        presenter.processInitPassengers(trainScheduleBookingPassData.getAdultPassenger(), trainScheduleBookingPassData.getInfantPassenger());
    }

    @Override
    public void renderPassenger(List<TrainPassengerViewModel> trainPassengerViewModels) {
        adapter.clearAllElements();
        adapter.addElement(trainPassengerViewModels);
    }

    @Override
    public void setCurrentListPassenger(List<TrainPassengerViewModel> trainPassengerViewModels) {
        trainParamPassenger.setTrainPassengerViewModelList(trainPassengerViewModels);
    }

    @Override
    public List<TrainPassengerViewModel> getCurrentPassengerList() {
        return trainParamPassenger.getTrainPassengerViewModelList();
    }

    @Override
    public void loadDetailSchedule(TrainScheduleViewModel trainScheduleViewModel, CardWithAction cardWithAction) {
        cardWithAction.setContentInfo(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.DEFAULT_VIEW_FORMAT, trainScheduleViewModel.getDepartureTimestamp()));
        cardWithAction.setSubContent(trainScheduleViewModel.getTrainName());
        String timeDepartureString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getDepartureTimestamp());
        String timeArrivalString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getArrivalTimestamp());
        cardWithAction.setSubContentInfo(" | " + timeDepartureString + " - " + timeArrivalString);
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
    public void setBirthdate(String birthdate) {
        birthdateBuyer.setText(birthdate);
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

    }

    @Override
    public void navigateToReview(TrainSoftbook trainSoftbook) {

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
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            TrainPassengerViewModel trainPassengerViewModel = data.getParcelableExtra(TrainBookingAddPassengerActivity.PASSENGER_DATA);
            presenter.updateDataPassengers(trainPassengerViewModel);
        }
    }
}
