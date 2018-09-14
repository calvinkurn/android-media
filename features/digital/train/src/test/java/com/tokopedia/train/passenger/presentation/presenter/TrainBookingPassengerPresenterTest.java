package com.tokopedia.train.passenger.presentation.presenter;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.domain.TrainProvider;
import com.tokopedia.train.common.domain.TrainTestScheduler;
import com.tokopedia.train.passenger.data.TrainBookingPassenger;
import com.tokopedia.train.passenger.domain.TrainSoftBookingUseCase;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.passenger.domain.requestmodel.TrainScheduleRequest;
import com.tokopedia.train.passenger.presentation.contract.TrainBookingPassengerContract;
import com.tokopedia.train.passenger.presentation.viewmodel.ProfileBuyerInfo;
import com.tokopedia.train.passenger.presentation.viewmodel.TrainPassengerViewModel;
import com.tokopedia.train.search.domain.GetDetailScheduleUseCase;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.verify;


/**
 * Created by nabillasabbaha on 30/08/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class TrainBookingPassengerPresenterTest {

    @Mock
    TrainBookingPassengerContract.View view;
    @Mock
    GetDetailScheduleUseCase getDetailScheduleUseCase;
    @Mock
    TrainSoftBookingUseCase trainSoftBookingUseCase;
    @Mock
    CardWithAction cardWithAction;

    TrainBookingPassengerPresenter presenter;
    TrainScheduleViewModel trainScheduleViewModel;
    TrainScheduleViewModel trainScheduleViewModelSpy;
    TrainScheduleRequest trainScheduleRequest;
    TrainScheduleViewModel trainReturnScheduleViewModel;
    TrainScheduleViewModel trainReturnScheduleViewModelSpy;
    TrainScheduleRequest trainReturnScheduleRequest;
    ProfileBuyerInfo profileBuyerInfoSpy;

    List<TrainPassengerViewModel> passengers;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new TestTrainBookingPassengerPresenter(getDetailScheduleUseCase, trainSoftBookingUseCase, new TrainTestScheduler());
        presenter.attachView(view);
    }

    @Test
    public void onDetailScheduleSingleTripLoadData_SelectedSchedule_Success() {
        //given
        spyDepartureTrainScheduleViewModel();
        String scheduleId = "20180831-GMR-BD-11G-B-B";
        String originCity = "Jakarta";
        String destinationCity = "Bandung";
        trainScheduleRequest = convertTripToRequestParam(trainScheduleViewModel);
        Mockito.when(getDetailScheduleUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.just(trainScheduleViewModelSpy));
        Mockito.when(view.getOriginCity()).thenReturn(originCity);
        Mockito.when(view.getDestinationCity()).thenReturn(destinationCity);
        Mockito.when(view.convertTripToRequestParam(trainScheduleViewModelSpy)).thenReturn(trainScheduleRequest);

        //when
        presenter.getDetailSchedule(scheduleId, cardWithAction);
        //then
        verify(view).showDepartureTripInfo();
        verify(view).setCityRouteTripInfo(cardWithAction, originCity, destinationCity);
        verify(view).hideReturnTripInfo();
        verify(view).setDepartureTripRequest(trainScheduleRequest);
        verify(view).loadDetailSchedule(trainScheduleViewModelSpy, cardWithAction);
    }

    @Test
    public void onDetailScheduleReturnTripLoadData_SelectedSchedule_Success() {
        //given
        spyReturnTrainScheduleViewModel();
        String scheduleId = "20180831-GMR-BD-11G-B-A";
        String originCity = "Jakarta";
        String destinationCity = "Bandung";
        trainReturnScheduleRequest = convertTripToRequestParam(trainReturnScheduleViewModel);
        Mockito.when(getDetailScheduleUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.just(trainReturnScheduleViewModelSpy));
        Mockito.when(view.getOriginCity()).thenReturn(originCity);
        Mockito.when(view.getDestinationCity()).thenReturn(destinationCity);
        Mockito.when(view.convertTripToRequestParam(trainReturnScheduleViewModelSpy)).thenReturn(trainReturnScheduleRequest);

        //when
        presenter.getDetailSchedule(scheduleId, cardWithAction);
        //then
        verify(view).setCityRouteTripInfo(cardWithAction, destinationCity, originCity);
        verify(view).showReturnTripInfo();
        verify(view).setReturnTripRequest(trainReturnScheduleRequest);
        verify(view).loadDetailSchedule(trainReturnScheduleViewModelSpy, cardWithAction);
    }

    private void spyDepartureTrainScheduleViewModel() {
        trainScheduleViewModel = new TrainScheduleViewModel();
        trainScheduleViewModel.setIdSchedule("20180831-GMR-BD-11G-B-B");
        trainScheduleViewModel.setAdultFare(100000);
        trainScheduleViewModel.setDisplayAdultFare("Rp 100.000");
        trainScheduleViewModel.setInfantFare(0);
        trainScheduleViewModel.setDisplayInfantFare("Rp 0");
        trainScheduleViewModel.setArrivalTimestamp("2018-08-31T11:40:00+07:00");
        trainScheduleViewModel.setDepartureTimestamp("2018-08-31T08:00:00+07:00");
        trainScheduleViewModel.setClassTrain("B");
        trainScheduleViewModel.setDisplayClass("Bisnis");
        trainScheduleViewModel.setSubclass("B");
        trainScheduleViewModel.setOrigin("BD");
        trainScheduleViewModel.setDestination("GMR");
        trainScheduleViewModel.setDisplayDuration("3j 40m");
        trainScheduleViewModel.setDuration(220);
        trainScheduleViewModel.setTrainKey("argo-gopar");
        trainScheduleViewModel.setTrainName("ARGO GOPAR");
        trainScheduleViewModel.setTrainNumber("11G");
        trainScheduleViewModel.setAvailableSeat(70);
        trainScheduleViewModel.setCheapestFlag(false);
        trainScheduleViewModel.setFastestFlag(false);
        trainScheduleViewModel.setReturnTrip(false);
        trainScheduleViewModelSpy = Mockito.spy(trainScheduleViewModel);
    }

    private void spyReturnTrainScheduleViewModel() {
        trainReturnScheduleViewModel = new TrainScheduleViewModel();
        trainReturnScheduleViewModel.setIdSchedule("20180831-GMR-BD-11G-B-A");
        trainReturnScheduleViewModel.setAdultFare(100000);
        trainReturnScheduleViewModel.setDisplayAdultFare("Rp 100.000");
        trainReturnScheduleViewModel.setInfantFare(0);
        trainReturnScheduleViewModel.setDisplayInfantFare("Rp 0");
        trainReturnScheduleViewModel.setArrivalTimestamp("2018-09-02T11:40:00+07:00");
        trainReturnScheduleViewModel.setDepartureTimestamp("2018-09-02T08:00:00+07:00");
        trainReturnScheduleViewModel.setClassTrain("B");
        trainReturnScheduleViewModel.setDisplayClass("Bisnis");
        trainReturnScheduleViewModel.setSubclass("B");
        trainReturnScheduleViewModel.setOrigin("GMR");
        trainReturnScheduleViewModel.setDestination("BD");
        trainReturnScheduleViewModel.setDisplayDuration("3j 40m");
        trainReturnScheduleViewModel.setDuration(220);
        trainReturnScheduleViewModel.setTrainKey("argo-gopar");
        trainReturnScheduleViewModel.setTrainName("ARGO GOPAR");
        trainReturnScheduleViewModel.setTrainNumber("11G");
        trainReturnScheduleViewModel.setAvailableSeat(70);
        trainReturnScheduleViewModel.setCheapestFlag(false);
        trainReturnScheduleViewModel.setFastestFlag(false);
        trainReturnScheduleViewModel.setReturnTrip(true);
        trainReturnScheduleViewModelSpy = Mockito.spy(trainReturnScheduleViewModel);
    }

    private TrainScheduleRequest convertTripToRequestParam(TrainScheduleViewModel trainScheduleViewModel) {
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

    @Test
    public void onDetailScheduleLoadData_SelectedSchedule_Failed() {
        //given
        String scheduleId = "20180831-GMR-BD-11G-B-A";
        String message = "Error";
        Mockito.when(getDetailScheduleUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.error(new MessageErrorException(message)));

        //when
        presenter.getDetailSchedule(scheduleId, cardWithAction);
        //then
        verify(view).hideDetailSchedule();
    }

    private void spyProfilBuyer() {
        ProfileBuyerInfo profileBuyerInfo = new ProfileBuyerInfo();
        profileBuyerInfo.setEmail("tkpd.qc@gmail.com");
        profileBuyerInfo.setFullname("Akun Test");
        profileBuyerInfo.setPhoneNumber("085647328787");
        profileBuyerInfoSpy = Mockito.spy(profileBuyerInfo);
    }

    /*
     *  value email : tkpd.qc@gmail.com
     *  value full name  : Akun Test
     *  value phone number : 085647328787
     */
    @Test
    public void onBuyerLoadData_SameAsLoginAccount_Success() {
        //given
        spyProfilBuyer();
        Mockito.when(view.getObservableProfileBuyerInfo()).thenReturn(Observable.just(profileBuyerInfoSpy));
        Mockito.when(view.getContactNameEt()).thenReturn("");
        Mockito.when(view.getEmailEt()).thenReturn("");
        Mockito.when(view.getPhoneNumberEt()).thenReturn("");
        //when
        presenter.getProfilBuyer();
        //then
        verify(view).setEmail("tkpd.qc@gmail.com");
        verify(view).setContactName("Akun Test");
        verify(view).setPhoneNumber("085647328787");
    }

    @Test
    public void onBuyerLoadData_SameAsLoginAccount_Failed() {
        //given
        String messageError = "Terjadi kesalahan pada server";
        Mockito.when(view.getObservableProfileBuyerInfo())
                .thenReturn(Observable.error(new MessageErrorException(messageError)));
        //when
        presenter.getProfilBuyer();
        //then
        verify(view).setEmail("");
        verify(view).setContactName("");
        verify(view).setPhoneNumber("");
    }

    @Test
    public void onPassengerAdultListLoadData_NotYetFilled_ShowDefaultPassengerList() {
        //given
        passengers = new ArrayList<>();
        passengers.clear();
        TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel();
        trainPassengerViewModel.setPassengerId(1);
        trainPassengerViewModel.setPaxType(TrainBookingPassenger.ADULT);
        trainPassengerViewModel.setHeaderTitle("Penumpang Dewasa");
        passengers.add(trainPassengerViewModel);

        //when
        presenter.processInitPassengers(1, 0);

        //then
        verify(view).renderPassengers(passengers);
        Assert.assertEquals(passengers.size(), 1);
        Assert.assertEquals(trainPassengerViewModel.getPaxType(), TrainBookingPassenger.ADULT);
        Mockito.verify(view).setCurrentListPassenger(passengers);
        Mockito.verify(view).renderPassengers(passengers);
    }

    @Test
    public void onPassengerAdultInfantListLoadData_NotYetFilled_ShowDefaultPassengerList() {
        //given
        setPassengers();

        //when
        presenter.processInitPassengers(1, 1);

        //then
        verify(view).renderPassengers(passengers);
        Assert.assertEquals(passengers.size(), 2);
        Assert.assertEquals(passengers.get(0).getPaxType(), TrainBookingPassenger.ADULT);
        Assert.assertEquals(passengers.get(1).getPaxType(), TrainBookingPassenger.INFANT);
        Mockito.verify(view).setCurrentListPassenger(passengers);
        Mockito.verify(view).renderPassengers(passengers);
    }

    @Test
    public void onPassengerListLoadData_HaveBeenFilled_ShowDataPassengerSelected() {
        //given
        setPassengers();
        Mockito.when(view.getCurrentPassengerList()).thenReturn(passengers);
        TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel();
        trainPassengerViewModel.setPassengerId(1);
        trainPassengerViewModel.setPaxType(TrainBookingPassenger.ADULT);
        trainPassengerViewModel.setHeaderTitle("Penumpang Dewasa");
        trainPassengerViewModel.setName("Bon Cabe");
        //when
        presenter.updateDataPassengers(trainPassengerViewModel);
        //then
        Assert.assertEquals(passengers.size(), 2);
        Assert.assertEquals(passengers.get(0).getPaxType(), TrainBookingPassenger.ADULT);
        Assert.assertEquals(passengers.get(0).getName(), "Bon Cabe");
        Mockito.verify(view).setCurrentListPassenger(passengers);
        Mockito.verify(view).renderPassengers(passengers);
    }

    private void setPassengers() {
        passengers = new ArrayList<>();
        TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel();
        trainPassengerViewModel.setPassengerId(1);
        trainPassengerViewModel.setPaxType(TrainBookingPassenger.ADULT);
        trainPassengerViewModel.setHeaderTitle("Penumpang Dewasa");
        passengers.add(trainPassengerViewModel);
        TrainPassengerViewModel trainPassengerViewModelInfant = new TrainPassengerViewModel();
        trainPassengerViewModelInfant.setPassengerId(2);
        trainPassengerViewModelInfant.setPaxType(TrainBookingPassenger.INFANT);
        trainPassengerViewModelInfant.setHeaderTitle("Penumpang Bayi");
        passengers.add(trainPassengerViewModelInfant);
    }

    @Test
    public void onClickChooseSeatButton_AllDataIsValid_SuccessNavigateToChooseSeatPage() {
        //given
        setPassengers();
        TrainSoftbook trainSoftbook = new TrainSoftbook();
        Mockito.when(trainSoftBookingUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.just(trainSoftbook));
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Mockito.verify(view).showPage();
        Mockito.verify(view).hideLoading();
        Mockito.verify(view).navigateToChooseSeat(trainSoftbook);
    }

    @Test
    public void onClickChooseSeatButton_AllDataNotValid_ShowErrorMessage() {
        //given
        setPassengers();
        TrainSoftbook trainSoftbook = new TrainSoftbook();
        Mockito.when(trainSoftBookingUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.just(trainSoftbook));
        Mockito.when(view.getContactNameEt()).thenReturn("Tkpd123");
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.train_passenger_contact_name_alpha_space_error);
    }

    @Test
    public void onClickPaymentButton_AllDataIsValid_SuccessNavigateToChooseSeatPage() {
        //given

        //when

        //then
    }

    @Test
    public void onClickPaymentButton_AllDataNotValid_ShowErrorMessage() {
        //given

        //when

        //then
    }

    public class TestTrainBookingPassengerPresenter extends TrainBookingPassengerPresenter {

        public TestTrainBookingPassengerPresenter(GetDetailScheduleUseCase getDetailScheduleUseCase,
                                                  TrainSoftBookingUseCase trainSoftBookingUseCase, TrainProvider trainProvider) {
            super(getDetailScheduleUseCase, trainSoftBookingUseCase, trainProvider);
        }

        @Override
        protected List<TrainPassengerViewModel> initPassenger(int adultPassengers, int infantPassengers) {
            return passengers;
        }
    }

}