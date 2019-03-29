package com.tokopedia.common.travel.presentation.presenter;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.common.travel.domain.DeleteTravelPassengerUseCase;
import com.tokopedia.common.travel.domain.GetTravelPassengersUseCase;
import com.tokopedia.common.travel.domain.provider.TravelTestScheduler;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerEditContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
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
public class TravelPassengerEditPresenterTest {

    @Mock
    TravelPassengerEditContract.View view;
    @Mock
    GetTravelPassengersUseCase getTravelPassengersUseCase;
    @Mock
    DeleteTravelPassengerUseCase deleteTravelPassengerUseCase;

    TravelPassengerEditPresenter presenter;
    List<TravelPassenger> travelPassengerList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new TravelPassengerEditPresenter(getTravelPassengersUseCase,
                deleteTravelPassengerUseCase, new TravelTestScheduler());
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
        travelPassenger.setId("uuid_pass_873a25225ed14ee1bb7d20cfba89fd61");

        TravelPassenger travelPassenger2 = new TravelPassenger();
        travelPassenger2.setIdLocal(2);
        travelPassenger2.setIdNumber("12726378261");
        travelPassenger2.setIdPassenger("27676");
        travelPassenger2.setName("Test Buyer");
        travelPassenger2.setFirstName("Test");
        travelPassenger2.setLastName("Buyer");
        travelPassenger2.setId("uuid_pass_873a25225ed14ee1bb7d20cfba89ffr1");
        travelPassenger2.setSelected(false);
        travelPassengerList.add(travelPassenger2);
    }

    public void onLoadPassengerList_OpenEditPassengerList_SuccessGetPassengerList() {
        //given
        setPassengerList();
        travelPassengerList.get(0).setName("Test New");
        travelPassengerList.get(0).setFirstName("Test");
        travelPassengerList.get(0).setLastName("New");
        Mockito.when(getTravelPassengersUseCase.createObservable(RequestParams.EMPTY))
                .thenReturn(Observable.just(travelPassengerList));
        //when
        presenter.getPassengerList();
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
        String messageError = "Terjadi kesalahan pada server";
        TravelPassenger travelPassenger = new TravelPassenger();
        travelPassenger.setIdPassenger("TesToped1234");
        MessageErrorException exception = new MessageErrorException(messageError);
        Mockito.when(view.getTravelPassengerBooking()).thenReturn(travelPassenger);
        Mockito.when(getTravelPassengersUseCase.createObservable(RequestParams.EMPTY))
                .thenReturn(Observable.error(exception));
        //when
        presenter.getPassengerList();
        //then
        Mockito.verify(view).showProgressBar();
        Mockito.verify(view).hideProgressBar();
        Mockito.verify(view).showMessageErrorInSnackBar(exception);
    }

    @Test
    public void onDeletePassenger_DeleteOnePassenger_PassengerDeletedAndShowSnackbar() {
        //given
        setPassengerList();
        travelPassengerList.get(0).setName("Toped Buyer");
        travelPassengerList.get(0).setFirstName("Toped");
        travelPassengerList.get(0).setLastName("Buyer");
        travelPassengerList.get(1).setName("Toped Seller");
        travelPassengerList.get(1).setFirstName("Toped");
        travelPassengerList.get(1).setLastName("Seller");
        Mockito.when(deleteTravelPassengerUseCase.createObservable(Mockito.any()))
                .thenReturn(Observable.just(true));
        //when
        presenter.deletePassenger("TesToped1234", travelPassengerList.get(0).getId(), travelPassengerList.get(0).getTravelId());
        //then
        Mockito.verify(view).showProgressBar();
        Mockito.verify(view).hideProgressBar();
        Mockito.verify(view).successDeletePassenger();
    }

    @Test
    public void onDeletePassenger_DataError_ShowErrorSnackbar() {
        //given
        String messageError = "Terjadi kesalahan pada server";
        TravelPassenger travelPassenger = new TravelPassenger();
        travelPassenger.setIdPassenger("TesToped1234");
        MessageErrorException exception = new MessageErrorException(messageError);
        Mockito.when(view.getTravelPassengerBooking()).thenReturn(travelPassenger);
        Mockito.when(getTravelPassengersUseCase.createObservable(RequestParams.EMPTY))
                .thenReturn(Observable.error(exception));
        //when
        presenter.getPassengerList();
        //then
        Mockito.verify(view).showProgressBar();
        Mockito.verify(view).hideProgressBar();
        Mockito.verify(view).showMessageErrorInSnackBar(exception);
    }
}