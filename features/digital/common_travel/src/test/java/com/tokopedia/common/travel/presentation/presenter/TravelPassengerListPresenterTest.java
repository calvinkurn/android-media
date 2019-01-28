package com.tokopedia.common.travel.presentation.presenter;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.domain.GetTravelPassengersUseCase;
import com.tokopedia.common.travel.domain.UpdateTravelPassengerUseCase;
import com.tokopedia.common.travel.domain.provider.TravelTestScheduler;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerListContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.utils.typedef.TravelBookingPassenger;
import com.tokopedia.usecase.RequestParams;

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

/**
 * Created by nabillasabbaha on 19/11/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class TravelPassengerListPresenterTest {

    @Mock
    TravelPassengerListContract.View view;
    @Mock
    GetTravelPassengersUseCase getTravelPassengersUseCase;
    @Mock
    UpdateTravelPassengerUseCase updateTravelPassengerUseCase;

    TravelPassengerListPresenter presenter;
    List<TravelPassenger> travelPassengerList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new TravelPassengerListPresenter(getTravelPassengersUseCase,
                updateTravelPassengerUseCase, new TravelTestScheduler());
        presenter.attachView(view);
    }

    private void setPassengerList() {
        travelPassengerList = new ArrayList<>();
        TravelPassenger travelPassenger = new TravelPassenger();
        travelPassenger.setIdLocal(1);
        travelPassenger.setIdNumber("1234567890");
        travelPassenger.setIdPassenger("27675");
        travelPassenger.setName("Test Seller");
        travelPassenger.setFirstName("Test");
        travelPassenger.setLastName("Seller");
        travelPassenger.setSelected(false);
        travelPassengerList.add(travelPassenger);
        travelPassenger.setId("");

        TravelPassenger travelPassenger2 = new TravelPassenger();
        travelPassenger2.setIdLocal(2);
        travelPassenger2.setIdNumber("12726378261");
        travelPassenger2.setIdPassenger("27676");
        travelPassenger2.setName("Test Buyer");
        travelPassenger2.setFirstName("Test");
        travelPassenger2.setLastName("Buyer");
        travelPassenger2.setId("");
        travelPassenger2.setSelected(false);
        travelPassengerList.add(travelPassenger2);
    }

    /**
     * first time open passenger list means after user log in to the account
     */
    @Test
    public void onLoadPassengerList_FirstTimeOpenPassengerList_SuccessGetPassengerList() {
        //given
        setPassengerList();
        Mockito.when(getTravelPassengersUseCase.createObservable(RequestParams.EMPTY))
                .thenReturn(Observable.just(travelPassengerList));
        //when
        presenter.getPassengerList(true, travelPassengerList.get(0).getIdLocal(),travelPassengerList.get(0).getIdPassenger());
        //then
        Assert.assertEquals(travelPassengerList.size(), 2);

        Mockito.verify(view).showProgressBar();
        Mockito.verify(view).hideProgressBar();
        Mockito.verify(view).renderPassengerList(travelPassengerList);
    }

    /**
     * second time dst open passenger list means no need reset database passenger list
     */
    @Test
    public void onLoadPassengerList_SecondTimeOpenPassengerList_SuccessGetPassengerList() {
        //given
        setPassengerList();
        travelPassengerList.get(0).setName("Test New");
        travelPassengerList.get(0).setFirstName("Test");
        travelPassengerList.get(0).setLastName("New");
        Mockito.when(getTravelPassengersUseCase.createObservable(RequestParams.EMPTY))
                .thenReturn(Observable.just(travelPassengerList));
        //when
        presenter.getPassengerList(false, travelPassengerList.get(0).getIdLocal(),travelPassengerList.get(0).getIdPassenger());
        //then
        Assert.assertEquals(travelPassengerList.size(), 2);
        Assert.assertEquals(travelPassengerList.get(0).getName(), "Test New");

        Mockito.verify(view).showProgressBar();
        Mockito.verify(view).hideProgressBar();
        Mockito.verify(view).renderPassengerList(travelPassengerList);
    }

    @Test
    public void onLoadPassengerList_DataError_ShowErrorSnackbar() {
        //given
        setPassengerList();
        String messageError = "Terjadi kesalahan pada server";
        MessageErrorException exception = new MessageErrorException(messageError);
        Mockito.when(getTravelPassengersUseCase.createObservable(RequestParams.EMPTY))
                .thenReturn(Observable.error(exception));
        //when
        presenter.getPassengerList(true, travelPassengerList.get(0).getIdLocal(),travelPassengerList.get(0).getIdPassenger());
        //then
        Mockito.verify(view).showProgressBar();
        Mockito.verify(view).hideProgressBar();
        Mockito.verify(view).showMessageErrorInSnackBar(exception);
    }

    @Test
    public void onSelectPassenger_PaxPassengerAdultNotSameAndDataValid_ShowSnackbarError() {
        //given
        setPassengerList();
        travelPassengerList.get(0).setPaxType(TravelBookingPassenger.ADULT);
        travelPassengerList.get(1).setPaxType(TravelBookingPassenger.INFANT);
        //when
        presenter.selectPassenger(travelPassengerList.get(0), travelPassengerList.get(1));
        //then
        Assert.assertNotEquals(travelPassengerList.get(0).getPaxType(), travelPassengerList.get(1).getPaxType());
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_error_msg_choose_passenger_adult);
    }

    @Test
    public void onSelectPassenger_PaxPassengerInfantNotSameAndDataValid_ShowSnackbarError() {
        //given
        setPassengerList();
        travelPassengerList.get(0).setPaxType(TravelBookingPassenger.INFANT);
        travelPassengerList.get(1).setPaxType(TravelBookingPassenger.ADULT);
        //when
        presenter.selectPassenger(travelPassengerList.get(0), travelPassengerList.get(1));
        //then
        Assert.assertNotEquals(travelPassengerList.get(0).getPaxType(), travelPassengerList.get(1).getPaxType());
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_error_msg_choose_passenger_infant);
    }

    @Test
    public void onSelectPassenger_PaxPassengerAnythingNotSameAndDataValid_ShowSnackbarError() {
        //given
        setPassengerList();
        travelPassengerList.get(0).setPaxType(4);
        travelPassengerList.get(1).setPaxType(TravelBookingPassenger.ADULT);
        //when
        presenter.selectPassenger(travelPassengerList.get(0), travelPassengerList.get(1));
        //then
        Assert.assertNotEquals(4, travelPassengerList.get(1).getPaxType());
        Mockito.verify(view).showMessageErrorInSnackBar(R.string.travel_error_msg_choose_passenger);
    }

    @Test
    public void onSelectPassenger_PaxPassengerSameAndDataValid_PassengerSelected() {
        //given
        setPassengerList();
        travelPassengerList.get(0).setPaxType(TravelBookingPassenger.ADULT);
        travelPassengerList.get(1).setPaxType(TravelBookingPassenger.ADULT);
        //when
        presenter.selectPassenger(travelPassengerList.get(0), travelPassengerList.get(1));
        //then
        Assert.assertEquals(travelPassengerList.get(0).getPaxType(), travelPassengerList.get(1).getPaxType());
        Mockito.verify(view).onClickSelectPassenger(travelPassengerList.get(1));
    }

    @Test
    public void onSelectPassenger_PaxPassengerSameAndDataNotValid_ShowErrorPassengerNotValid() {
        //given
        setPassengerList();
        travelPassengerList.get(0).setPaxType(TravelBookingPassenger.ADULT);
        travelPassengerList.get(1).setPaxType(TravelBookingPassenger.ADULT);
        travelPassengerList.get(1).setIdNumber("");
        //when
        presenter.selectPassenger(travelPassengerList.get(0), travelPassengerList.get(1));
        //then
        Assert.assertEquals(travelPassengerList.get(0).getPaxType(), travelPassengerList.get(1).getPaxType());
        Assert.assertEquals(travelPassengerList.get(1).getIdNumber(), "");

        Mockito.verify(view).showActionErrorInSnackBar(travelPassengerList.get(1), R.string.travel_error_msg_pick_passenger_data_not_valid);
    }

    @Test
    public void onUpdatePassenger_PassengerSelected_SuccessUpdate() {
        //given
        setPassengerList();
        Mockito.when(updateTravelPassengerUseCase.createObservable(
                updateTravelPassengerUseCase.createRequestParams("27675", true)))
                .thenReturn(Observable.just(true));
        //when
        presenter.updatePassenger("27675", true);
        //then
        Mockito.verify(view).successUpdatePassengerDb();
    }
}