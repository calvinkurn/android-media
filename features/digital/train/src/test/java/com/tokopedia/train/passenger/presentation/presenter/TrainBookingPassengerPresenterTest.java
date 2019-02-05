package com.tokopedia.train.passenger.presentation.presenter;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.common.travel.utils.TravelPassengerValidator;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.data.interceptor.TrainNetworkException;
import com.tokopedia.train.common.data.interceptor.model.TrainError;
import com.tokopedia.train.common.domain.TrainProvider;
import com.tokopedia.train.common.domain.TrainTestScheduler;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.common.util.TrainNetworkErrorConstant;
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
import java.util.Date;
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
    TrainSoftbook trainSoftbook;
    private String validName;
    private String validEmail;
    private String validPhoneNumber;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new TestTrainBookingPassengerPresenter(getDetailScheduleUseCase, trainSoftBookingUseCase,
                new TrainTestScheduler(), new TravelPassengerValidator());
        presenter.attachView(view);
        trainSoftbook = new TrainSoftbook();
        validName = "Cincin Seller";
        validPhoneNumber = "081234567892";
        validEmail = "tkpd.qc@gmail.com";
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
        trainPassengerViewModel.setIdLocal(1);
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
        trainPassengerViewModel.setIdLocal(1);
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
        trainPassengerViewModel.setIdLocal(1);
        trainPassengerViewModel.setPaxType(TrainBookingPassenger.ADULT);
        trainPassengerViewModel.setHeaderTitle("Penumpang Dewasa");
        passengers.add(trainPassengerViewModel);
        TrainPassengerViewModel trainPassengerViewModelInfant = new TrainPassengerViewModel();
        trainPassengerViewModelInfant.setIdLocal(2);
        trainPassengerViewModelInfant.setPaxType(TrainBookingPassenger.INFANT);
        trainPassengerViewModelInfant.setHeaderTitle("Penumpang Bayi");
        passengers.add(trainPassengerViewModelInfant);
    }

    private void setDataForBookPassengerList() {
        setPassengers();
        Mockito.when(trainSoftBookingUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.just(trainSoftbook));
    }

    @Test
    public void onClickChooseSeatButton_BuyerNameEmptyNotValid_ShowErrorMessage() {
        //given
        String notValidName = "";
        setDataForBookPassengerList();
        Mockito.when(view.getContactNameEt()).thenReturn(notValidName);
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Assert.assertEquals(notValidName.length(), 0);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.train_passenger_contact_name_empty_error);
    }

    @Test
    public void onClickChooseSeatButton_BuyerNameReachLimitNotValid_ShowErrorMessage() {
        //given
        String notValidName = "sdajndsajkndjasndsjkadnskajdnkasjdnkasjndaksjndkasjndaksjdnaksjdn";
        setDataForBookPassengerList();
        Mockito.when(view.getContactNameEt()).thenReturn(notValidName);
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Assert.assertEquals(notValidName.length(), 65);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.train_passenger_contact_name_max);
    }

    @Test
    public void onClickChooseSeatButton_BuyerNameAlphanumericNotValid_ShowErrorMessage() {
        //given
        setDataForBookPassengerList();
        Mockito.when(view.getContactNameEt()).thenReturn("Tkpd123");
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.train_passenger_contact_name_alpha_space_error);
    }

    @Test
    public void onClickChooseSeatButton_EmailEmptyNotValid_ShowErrorMessage() {
        //given
        String notValidEmail = "";
        setDataForBookPassengerList();
        Mockito.when(view.getContactNameEt()).thenReturn("Cincin Seller");
        Mockito.when(view.getPhoneNumberEt()).thenReturn("081234567892");
        Mockito.when(view.getEmailEt()).thenReturn("");
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Assert.assertEquals(notValidEmail.length(), 0);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.train_passenger_contact_email_empty_error);

    }

    @Test
    public void onClickChooseSeatButton_EmailWithSpecialCharNotValid_ShowErrorMessage() {
        //given
        setDataForBookPassengerList();
        Mockito.when(view.getContactNameEt()).thenReturn("Cincin Seller");
        Mockito.when(view.getPhoneNumberEt()).thenReturn("081234567892");
        Mockito.when(view.getEmailEt()).thenReturn("tkpd.qc+47@gmail.com");
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.train_passenger_contact_email_invalid_error);
    }

    @Test
    public void onClickChooseSeatButton_PhoneNumberAlphanumericNotValid_ShowErrorMessage() {
        //given
        String phoneNumber = "0821376h1iuh";
        setDataForBookPassengerList();
        Mockito.when(view.getContactNameEt()).thenReturn("Cincin Seller");
        Mockito.when(view.getEmailEt()).thenReturn("tkpd.qc@gmail.com");
        Mockito.when(view.getPhoneNumberEt()).thenReturn(phoneNumber);
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_invalid_error);
    }

    @Test
    public void onClickChooseSeatButton_PhoneNumberReachMaxNotValid_ShowErrorMessage() {
        //given
        String phoneNumber = "0812345678910123";
        setDataForBookPassengerList();
        Mockito.when(view.getContactNameEt()).thenReturn("Cincin Seller");
        Mockito.when(view.getEmailEt()).thenReturn("tkpd.qc@gmail.com");
        Mockito.when(view.getPhoneNumberEt()).thenReturn(phoneNumber);
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Assert.assertEquals(phoneNumber.length(), 16);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_max_length_error);
    }

    @Test
    public void onClickChooseSeatButton_PhoneNumberReachMinNotValid_ShowErrorMessage() {
        //given
        String phoneNumber = "081234";
        setDataForBookPassengerList();
        Mockito.when(view.getContactNameEt()).thenReturn("Cincin Seller");
        Mockito.when(view.getEmailEt()).thenReturn("tkpd.qc@gmail.com");
        Mockito.when(view.getPhoneNumberEt()).thenReturn(phoneNumber);
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Assert.assertEquals(phoneNumber.length(), 6);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_min_length_error);
    }

    @Test
    public void onClickChooseSeatButton_PhoneNumberEmptyNotValid_ShowErrorMessage() {
        //given
        String phoneNumber = "";
        setDataForBookPassengerList();
        Mockito.when(view.getContactNameEt()).thenReturn("Cincin Seller");
        Mockito.when(view.getPhoneNumberEt()).thenReturn(phoneNumber);
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Assert.assertEquals(phoneNumber.length(), 0);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_empty_error);
    }

    @Test
    public void onClickChooseSeatButton_PassengerListEmpty_ShowErrorMessage() {
        //given
        setDataForBookPassengerList();
        passengers.get(0).setName(null);
        passengers.get(1).setName(null);
        Mockito.when(view.getContactNameEt()).thenReturn("Cincin Seller");
        Mockito.when(view.getPhoneNumberEt()).thenReturn("081234567892");
        Mockito.when(view.getEmailEt()).thenReturn("tkpd.qc@gmail.com");
        Mockito.when(view.getCurrentPassengerList()).thenReturn(passengers);
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Assert.assertNotNull(view.getContactNameEt());
        Assert.assertNotNull(view.getPhoneNumberEt());
        Assert.assertNotNull(view.getEmailEt());
        Assert.assertNull(passengers.get(0).getName());
        Assert.assertNull(passengers.get(1).getName());
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.train_passenger_passenger_not_fullfilled_error);
    }

    @Test
    public void onClickChooseSeatButton_AllDataIsValid_SuccessNavigateToChooseSeatPage() {
        //given
        setValidDataForBookingPassenger();
        Mockito.when(trainSoftBookingUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.just(trainSoftbook));
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Assert.assertNotNull(view.getContactNameEt());
        Assert.assertNotNull(view.getPhoneNumberEt());
        Assert.assertNotNull(view.getEmailEt());
        Assert.assertNotNull(passengers);
        Assert.assertEquals(validName.length(), 13);
        Assert.assertEquals(validPhoneNumber.length(), 12);
        Assert.assertEquals(passengers.size(), 2);
        Assert.assertNotNull(passengers.get(0).getName());
        Assert.assertNotNull(passengers.get(1).getName());

        Mockito.verify(view).hidePage();
        Mockito.verify(view).showLoading();
        Mockito.verify(view).showPage();
        Mockito.verify(view).hideLoading();
        Mockito.verify(view).navigateToChooseSeat(trainSoftbook);
    }

    @Test
    public void onClickChooseSeatButton_ErrorResponse_ShowErrorMessage() {
        //given
        String messageError = "Terjadi kesalahan pada server";
        MessageErrorException messageErrorException = new MessageErrorException(messageError);
        setValidDataForBookingPassenger();
        Mockito.when(trainSoftBookingUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.error(messageErrorException));
        //when
        presenter.onChooseSeatButtonClicked();
        //then
        Mockito.verify(view).showPage();
        Mockito.verify(view).hideLoading();
        Mockito.verify(view).showErrorSoftBooking(messageErrorException);
    }

    private void setValidDataForBookingPassenger() {
        setPassengers();
        passengers.get(0).setName("Cincin Seller");
        passengers.get(1).setName("Cincin Buyer");
        Mockito.when(view.getContactNameEt()).thenReturn(validName);
        Mockito.when(view.getPhoneNumberEt()).thenReturn(validPhoneNumber);
        Mockito.when(view.getEmailEt()).thenReturn(validEmail);
        Mockito.when(view.getCurrentPassengerList()).thenReturn(passengers);
    }

    @Test
    public void onClickPaymentButton_AllDataIsValid_SuccessNavigateToChooseSeatPage() {
        //given
        setValidDataForBookingPassenger();
        Mockito.when(trainSoftBookingUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.just(trainSoftbook));
        //when
        presenter.onSubmitButtonClicked();
        //then
        Assert.assertNotNull(view.getContactNameEt());
        Assert.assertNotNull(view.getPhoneNumberEt());
        Assert.assertNotNull(view.getEmailEt());
        Assert.assertNotNull(passengers);
        Assert.assertEquals(validName.length(), 13);
        Assert.assertEquals(validPhoneNumber.length(), 12);
        Assert.assertEquals(passengers.size(), 2);
        Assert.assertNotNull(passengers.get(0).getName());
        Assert.assertNotNull(passengers.get(1).getName());

        Mockito.verify(view).hidePage();
        Mockito.verify(view).showLoading();
        Mockito.verify(view).showPage();
        Mockito.verify(view).hideLoading();
        Mockito.verify(view).navigateToReview(trainSoftbook);
    }

    @Test
    public void onClickPaymentButton_ErrorResponse_ShowErrorMessage() {
        //given
        String messageError = "Terjadi kesalahan pada server";
        MessageErrorException messageErrorException = new MessageErrorException(messageError);
        setValidDataForBookingPassenger();
        Mockito.when(trainSoftBookingUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.error(messageErrorException));
        //when
        presenter.onSubmitButtonClicked();
        //then
        Mockito.verify(view).showPage();
        Mockito.verify(view).hideLoading();
        Mockito.verify(view).showErrorSoftBooking(messageErrorException);
    }

    @Test
    public void onClickPaymentButton_ErrorTrainSoldOutResponse_ShowErrorMessage() {
        //given
        setValidDataForBookingPassenger();
        List<TrainError> trainErrors = new ArrayList<>();
        trainErrors.add(new TrainError(TrainNetworkErrorConstant.SOLD_OUT));
        TrainNetworkException trainNetworkException = new TrainNetworkException("Tiket telah habis terjual", trainErrors);
        Mockito.when(trainSoftBookingUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.error(trainNetworkException));
        //when
        presenter.onSubmitButtonClicked();
        //then
        Mockito.verify(view).showPage();
        Mockito.verify(view).hideLoading();
        Mockito.verify(view).showNavigateToSearchDialog(trainNetworkException.getMessage());
    }

    @Test
    public void onClickPaymentButton_ErrorRouteNotFoundResponse_ShowErrorMessage() {
        //given
        setValidDataForBookingPassenger();
        List<TrainError> trainErrors = new ArrayList<>();
        trainErrors.add(new TrainError(TrainNetworkErrorConstant.RUTE_NOT_FOUND));
        TrainNetworkException trainNetworkException = new TrainNetworkException("Rute sudah tidak tersedia", trainErrors);
        Mockito.when(trainSoftBookingUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.error(trainNetworkException));
        //when
        presenter.onSubmitButtonClicked();
        //then
        Mockito.verify(view).showPage();
        Mockito.verify(view).hideLoading();
        Mockito.verify(view).showNavigateToSearchDialog(trainNetworkException.getMessage());
    }

    @Test
    public void onClickPaymentButton_ErrorTooManyBookingResponse_ShowErrorMessage() {
        //given
        setValidDataForBookingPassenger();
        List<TrainError> trainErrors = new ArrayList<>();
        trainErrors.add(new TrainError(TrainNetworkErrorConstant.TOO_MANY_SOFTBOOK));
        TrainNetworkException trainNetworkException = new TrainNetworkException("Too many did soft book", trainErrors);
        Mockito.when(trainSoftBookingUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.error(trainNetworkException));
        //when
        presenter.onSubmitButtonClicked();
        //then
        Mockito.verify(view).showPage();
        Mockito.verify(view).hideLoading();
        Mockito.verify(view).showNavigateToSearchDialog(trainNetworkException.getMessage());
    }

    @Test
    public void onClickPaymentButton_ErrorLessThan3HoursResponse_ShowErrorMessage() {
        //given
        setValidDataForBookingPassenger();
        List<TrainError> trainErrors = new ArrayList<>();
        trainErrors.add(new TrainError(TrainNetworkErrorConstant.LESS_THAN_3_HOURS));
        TrainNetworkException trainNetworkException = new TrainNetworkException("Pemesanan Anda kurang dari 3 jam dari waktu keberangkatan", trainErrors);
        Mockito.when(trainSoftBookingUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.error(trainNetworkException));
        //when
        presenter.onSubmitButtonClicked();
        //then
        Mockito.verify(view).showPage();
        Mockito.verify(view).hideLoading();
        Mockito.verify(view).showNavigateToSearchDialog(trainNetworkException.getMessage());
    }

    public class TestTrainBookingPassengerPresenter extends TrainBookingPassengerPresenter {

        public TestTrainBookingPassengerPresenter(GetDetailScheduleUseCase getDetailScheduleUseCase,
                                                  TrainSoftBookingUseCase trainSoftBookingUseCase,
                                                  TrainProvider trainProvider,
                                                  TravelPassengerValidator travelPassengerValidator) {
            super(getDetailScheduleUseCase, trainSoftBookingUseCase, trainProvider, travelPassengerValidator);
        }

        @Override
        protected List<TrainPassengerViewModel> initPassenger(int adultPassengers, int infantPassengers) {
            return passengers;
        }
    }

    @Test
    public void onCalculateUpperLowerBithDate_AdultPassenger_GetUpperLowerBirhtDate() {
        //given
        String lowerDate = "2015-12-03";
        Date departureDate = TrainDateUtil.stringToDate("2018-12-04");
        Mockito.when(view.getDepartureDate()).thenReturn(departureDate);
        //when
        presenter.calculateUpperLowerBirthDate(TrainBookingPassenger.ADULT);
        //then
        Mockito.verify(view).showUpperLowerBirthDate(lowerDate, "");
    }

    @Test
    public void onCalculateUpperLowerBithDate_InfantPassenger_GetUpperLowerBirhtDate() {
        //given
        String lowerDate = "2018-12-03";
        String upperDate = "2015-12-03";
        Date departureDate = TrainDateUtil.stringToDate("2018-12-04");
        Mockito.when(view.getDepartureDate()).thenReturn(departureDate);
        //when
        presenter.calculateUpperLowerBirthDate(TrainBookingPassenger.INFANT);
        //then
        Mockito.verify(view).showUpperLowerBirthDate(lowerDate, upperDate);
    }
}