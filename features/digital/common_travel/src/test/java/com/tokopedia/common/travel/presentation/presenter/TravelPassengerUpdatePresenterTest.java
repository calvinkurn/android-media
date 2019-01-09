package com.tokopedia.common.travel.presentation.presenter;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.domain.AddTravelPassengerUseCase;
import com.tokopedia.common.travel.domain.EditTravelPassengerUseCase;
import com.tokopedia.common.travel.domain.provider.TravelTestScheduler;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerUpdateContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.utils.TravelDateUtil;
import com.tokopedia.common.travel.utils.TravelPassengerValidator;
import com.tokopedia.common.travel.utils.typedef.TravelBookingPassenger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import rx.Observable;

/**
 * Created by nabillasabbaha on 27/11/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class TravelPassengerUpdatePresenterTest {

    @Mock
    TravelPassengerUpdateContract.View view;
    @Mock
    AddTravelPassengerUseCase addTravelPassengerUseCase;
    @Mock
    EditTravelPassengerUseCase editTravelPassengerUseCase;

    TravelPassengerUpdatePresenter presenter;
    TravelPassenger travelPassenger;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new TravelPassengerUpdatePresenter(addTravelPassengerUseCase,
                editTravelPassengerUseCase, new TravelTestScheduler(), new TravelPassengerValidator());
        presenter.attachView(view);
    }

    @Test
    public void onAddPassenger_NotYetChooseSalutation_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("");
        //when
        presenter.submitAddPassengerData();
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_salutation);
    }

    @Test
    public void onAddPassenger_FirstNameEmpty_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        String nameNotValid = "";
        Mockito.when(view.getFirstName()).thenReturn(nameNotValid);
        //when
        presenter.submitAddPassengerData();
        //then
        Assert.assertEquals(nameNotValid.length(), 0);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_first_name);
    }

    @Test
    public void onAddPassenger_FirstNameMoreThan60Char_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        String nameNotValid = "sdakndadnajkdnakjdnakjsndajksndkjasndkjsandkjasndaksjndakjsnda";
        Mockito.when(view.getFirstName()).thenReturn(nameNotValid);
        //when
        presenter.submitAddPassengerData();
        //then
        Assert.assertEquals(nameNotValid.length(), 62);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_max);
    }

    @Test
    public void onAddPassenger_FirstNameAlphanumeric_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        String nameNotValid = "1234jsndajksndkjasndkjsandkjasndaksjndakjsnda";
        Mockito.when(view.getFirstName()).thenReturn(nameNotValid);
        //when
        presenter.submitAddPassengerData();
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_containt_alphabet);
    }

    @Test
    public void onAddPassenger_LastNameEmpty_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        String nameNotValid = "";
        Mockito.when(view.getLastName()).thenReturn(nameNotValid);
        //when
        presenter.submitAddPassengerData();
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_last_name);
    }

    @Test
    public void onAddPassenger_LastNameMoreThan60Char_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        String nameNotValid = "sdakndadnajkdnakjdnakjsndajksndkjasndkjsandkjasndaksjndakjsnda";
        Mockito.when(view.getLastName()).thenReturn(nameNotValid);
        //when
        presenter.submitAddPassengerData();
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_max);
    }

    @Test
    public void onAddPassenger_LastNameMoreThan2Words_ShowError() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        String nameNotValid = "ksdnak askdnakjdn";
        Mockito.when(view.getLastName()).thenReturn(nameNotValid);
        //when
        presenter.submitAddPassengerData();
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_contact_last_name_word);
    }

    @Test
    public void onAddPassenger_LastNameAlphanumeric_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        String nameNotValid = "12345dnakjsndajksndkjasndkjsandkjasndaksjndakjsnda";
        Mockito.when(view.getLastName()).thenReturn(nameNotValid);
        //when
        presenter.submitAddPassengerData();
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_containt_alphabet);
    }

    @Test
    public void onAddPassenger_InfantBirthdateEmpty_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getBirthdate()).thenReturn("");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.INFANT);
        //when
        presenter.submitAddPassengerData();
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_birthdate_empty);
    }

    @Test
    public void onAddPassenger_IdNumberEmpty_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        String idNumberNotValid = "";
        Mockito.when(view.getIdentityNumber()).thenReturn(idNumberNotValid);
        //when
        presenter.submitAddPassengerData();
        //then
        Assert.assertEquals(idNumberNotValid.length(), 0);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number);
    }

    @Test
    public void onAddPassenger_IdNumberLessThan5Char_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        String idNumberNotValid = "1234";
        Mockito.when(view.getIdentityNumber()).thenReturn(idNumberNotValid);
        //when
        presenter.submitAddPassengerData();
        //then
        Assert.assertEquals(idNumberNotValid.length(), 4);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number_min);
    }

    @Test
    public void onAddPassenger_IdNumberMoreThanMaxLimit_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        String idNumberNotValid = "12345678910234567890123";
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        Mockito.when(view.getIdentityNumber()).thenReturn(idNumberNotValid);
        //when
        presenter.submitAddPassengerData();
        //then
        Assert.assertEquals(idNumberNotValid.length(), 23);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number_max);
    }

    @Test
    public void onAddPassenger_IdNumberHaveSpecialChar_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        String idNumberNotValid = "12345678910BD?-&3";
        Mockito.when(view.getIdentityNumber()).thenReturn(idNumberNotValid);
        //when
        presenter.submitAddPassengerData();
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_alphanumeric);
    }

    //===========================================================================

    @Test
    public void onEditPassenger_NotYetChooseSalutation_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("");
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_salutation);
    }

    @Test
    public void onEditPassenger_FirstNameEmpty_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        String nameNotValid = "";
        Mockito.when(view.getFirstName()).thenReturn(nameNotValid);
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Assert.assertEquals(nameNotValid.length(), 0);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_first_name);
    }

    @Test
    public void onEditPassenger_FirstNameMoreThan60Char_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        String nameNotValid = "sdakndadnajkdnakjdnakjsndajksndkjasndkjsandkjasndaksjndakjsnda";
        Mockito.when(view.getFirstName()).thenReturn(nameNotValid);
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Assert.assertEquals(nameNotValid.length(), 62);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_max);
    }

    @Test
    public void onEditPassenger_FirstNameAlphanumeric_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        String nameNotValid = "1234jsndajksndkjasndkjsandkjasndaksjndakjsnda";
        Mockito.when(view.getFirstName()).thenReturn(nameNotValid);
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_containt_alphabet);
    }

    @Test
    public void onEditPassenger_LastNameEmpty_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        String nameNotValid = "";
        Mockito.when(view.getLastName()).thenReturn(nameNotValid);
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_last_name);
    }

    @Test
    public void onEditPassenger_LastNameMoreThan60Char_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        String nameNotValid = "sdakndadnajkdnakjdnakjsndajksndkjasndkjsandkjasndaksjndakjsnda";
        Mockito.when(view.getLastName()).thenReturn(nameNotValid);
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_max);
    }

    @Test
    public void onEditPassenger_LastNameAlphanumeric_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        String nameNotValid = "12345dnakjsndajksndkjasndkjsandkjasndaksjndakjsnda";
        Mockito.when(view.getLastName()).thenReturn(nameNotValid);
        //when
        presenter.submitEditPassengerData("123455");
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_containt_alphabet);
    }

    @Test
    public void onEditPassenger_LastNameMoreThanTwoWords_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        String nameNotValid = "Buyer Seller";
        Mockito.when(view.getLastName()).thenReturn(nameNotValid);
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_contact_last_name_word);
    }

    @Test
    public void onEditPassenger_InfantBirthdateEmpty_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getBirthdate()).thenReturn("");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.INFANT);

        //when
        presenter.submitEditPassengerData("123456");
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_birthdate_empty);
    }

    @Test
    public void onEditPassenger_IdNumberEmpty_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        String idNumberNotValid = "";
        Mockito.when(view.getIdentityNumber()).thenReturn(idNumberNotValid);
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Assert.assertEquals(idNumberNotValid.length(), 0);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number);
    }

    @Test
    public void onEditPassenger_IdNumberLessThan5Char_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        String idNumberNotValid = "1234";
        Mockito.when(view.getIdentityNumber()).thenReturn(idNumberNotValid);
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Assert.assertEquals(idNumberNotValid.length(), 4);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number_min);
    }

    @Test
    public void onEditPassenger_IdNumberMoreThanMaxLimit_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        String idNumberNotValid = "12345678910234567890123";
        Mockito.when(view.getIdentityNumber()).thenReturn(idNumberNotValid);
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Assert.assertEquals(idNumberNotValid.length(), 23);
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number_max);
    }

    @Test
    public void onEditPassenger_IdNumberHaveSpecialChar_ShowErrorSnackbar() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        String idNumberNotValid = "12345678910BD?-&3";
        Mockito.when(view.getIdentityNumber()).thenReturn(idNumberNotValid);
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_alphanumeric);
    }

    //===========================================================================


    private void setTravelPassenger() {
        travelPassenger = new TravelPassenger();
        travelPassenger.setName("Toped Buyer");
        travelPassenger.setFirstName("Toped");
        travelPassenger.setLastName("Buyer");
        travelPassenger.setIdNumber("12345678910");
        travelPassenger.setBirthDate("1994-04-11T00:00:00Z");
        travelPassenger.setTitle(1);
        travelPassenger.setPaxType(1);
    }

    @Test
    public void onAddPassenger_AllDataAdultValid_SuccessAddPassenger() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getBirthdate()).thenReturn("24 Juli 1994");
        Mockito.when(view.getIdentityNumber()).thenReturn("12345678910");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        setTravelPassenger();
        Mockito.when(addTravelPassengerUseCase.createObservable(Mockito.any()))
                .thenReturn(Observable.just(travelPassenger));
        //when
        presenter.submitAddPassengerData();
        //then
        Mockito.verify(view).navigateToPassengerList();
    }

    @Test
    public void onAddPassenger_AllDataAdultValidWithSpaceLastName_SuccessAddPassenger() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer ");
        Mockito.when(view.getBirthdate()).thenReturn("24 Juli 1994");
        Mockito.when(view.getIdentityNumber()).thenReturn("12345678910");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        setTravelPassenger();
        Mockito.when(addTravelPassengerUseCase.createObservable(Mockito.any()))
                .thenReturn(Observable.just(travelPassenger));
        //when
        presenter.submitAddPassengerData();
        //then
        Mockito.verify(view).navigateToPassengerList();
    }

    @Test
    public void onAddPassenger_AllDataInfantValid_SuccessAddPassenger() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getBirthdate()).thenReturn("24 Juli 1994");
        Mockito.when(view.getIdentityNumber()).thenReturn("24071994");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.INFANT);
        setTravelPassenger();
        Mockito.when(addTravelPassengerUseCase.createObservable(Mockito.any()))
                .thenReturn(Observable.just(travelPassenger));
        //when
        presenter.submitAddPassengerData();
        //then
        Mockito.verify(view).navigateToPassengerList();
    }

    @Test
    public void onAddPassenger_DataError_FailedAddPassenger() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getBirthdate()).thenReturn("24 Juli 1994");
        Mockito.when(view.getIdentityNumber()).thenReturn("12345678910");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        setTravelPassenger();
        String messageError = "Terjadi kesalahan pada server";
        MessageErrorException exception = new MessageErrorException(messageError);
        Mockito.when(addTravelPassengerUseCase.createObservable(Mockito.any()))
                .thenReturn(Observable.error(exception));
        //when
        presenter.submitAddPassengerData();
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(exception);
    }

    @Test
    public void onEditPassenger_AllDataAdultValid_SuccessEditPassenger() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getIdentityNumber()).thenReturn("12345678910");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        Mockito.when(editTravelPassengerUseCase.createObservable(Mockito.any()))
                .thenReturn(Observable.just(true));
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Mockito.verify(view).navigateToPassengerList();
    }

    @Test
    public void onEditPassenger_AllDataInfantValid_SuccessEditPassenger() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getBirthdate()).thenReturn("24 Juli 1994");
        Mockito.when(view.getIdentityNumber()).thenReturn("24071994");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.INFANT);
        Mockito.when(editTravelPassengerUseCase.createObservable(Mockito.any()))
                .thenReturn(Observable.just(true));
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Mockito.verify(view).navigateToPassengerList();
    }

    @Test
    public void onEditPassenger_DataError_FailedEditPassenger() {
        //given
        Mockito.when(view.getSalutationTitle()).thenReturn("Tuan");
        Mockito.when(view.getFirstName()).thenReturn("Toped");
        Mockito.when(view.getLastName()).thenReturn("Buyer");
        Mockito.when(view.getIdentityNumber()).thenReturn("12345678910");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        setTravelPassenger();
        String messageError = "Terjadi kesalahan pada server";
        MessageErrorException exception = new MessageErrorException(messageError);
        Mockito.when(editTravelPassengerUseCase.createObservable(Mockito.any()))
                .thenReturn(Observable.error(exception));
        //when
        presenter.submitEditPassengerData("123456");
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(exception);
    }

    @Test
    public void onChangeBirthDate_PassengerInfantChooseWrongDate_ShowSnackbarError() {
        //given
        int dateSelected = 21;
        int monthSelected = 10;
        int yearSelected = 1995;
        Date lowerBirthDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, "2018-10-02");
        Date upperBirthDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, "2015-10-02");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.INFANT);
        Mockito.when(view.getLowerBirthDate()).thenReturn(lowerBirthDate);
        Mockito.when(view.getUpperBirthDate()).thenReturn(upperBirthDate);
        //when
        presenter.onChangeBirthdate(yearSelected, monthSelected, dateSelected);
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_error_msg_pick_infant_passenger);
    }

    @Test
    public void onChangeBirthDate_PassengerInfantChooseRightDate_SelectedDate() {
        //given
        int dateSelected = 21;
        int monthSelected = 10;
        int yearSelected = 2015;
        Date lowerBirthDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, "2018-10-02");
        Date upperBirthDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, "2015-10-02");
        Date dateFullSelected = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, "2015-11-21");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.INFANT);
        Mockito.when(view.getLowerBirthDate()).thenReturn(lowerBirthDate);
        Mockito.when(view.getUpperBirthDate()).thenReturn(upperBirthDate);
        //when
        presenter.onChangeBirthdate(yearSelected, monthSelected, dateSelected);
        //then
        Mockito.verify(view).showBirthdateChange(dateFullSelected);
    }

    @Test
    public void onChangeBirthDate_PassengerAdultChooseWrongDate_ShowSnackbarError() {
        //given
        int dateSelected = 21;
        int monthSelected = 10;
        int yearSelected = 2015;
        Date lowerBirthDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, "2015-10-02");
        Date dateFullSelected = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, "2015-10-21");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        Mockito.when(view.getLowerBirthDate()).thenReturn(lowerBirthDate);
        //when
        presenter.onChangeBirthdate(yearSelected, monthSelected, dateSelected);
        //then
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_error_msg_pick_adult_passenger);
    }

    @Test
    public void onChangeBirthDate_PassengerAdultChooseRightDate_SelectedDate() {
        //given
        int dateSelected = 21;
        int monthSelected = 9;
        int yearSelected = 1995;
        Date lowerBirthDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, "2015-10-02");
        Date dateFullSelected = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, "1995-10-21");
        Mockito.when(view.getPaxType()).thenReturn(TravelBookingPassenger.ADULT);
        Mockito.when(view.getLowerBirthDate()).thenReturn(lowerBirthDate);
        //when
        presenter.onChangeBirthdate(yearSelected, monthSelected, dateSelected);
        //then
        Mockito.verify(view).showBirthdateChange(dateFullSelected);
    }
}