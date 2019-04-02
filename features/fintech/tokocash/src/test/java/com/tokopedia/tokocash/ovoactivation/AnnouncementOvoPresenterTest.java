package com.tokopedia.tokocash.ovoactivation;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.tokocash.common.WalletTestScheduler;
import com.tokopedia.tokocash.ovoactivation.domain.CheckNumberOvoUseCase;
import com.tokopedia.tokocash.ovoactivation.view.CheckPhoneOvoModel;
import com.tokopedia.tokocash.ovoactivation.view.IntroOvoContract;
import com.tokopedia.tokocash.ovoactivation.view.IntroOvoPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

/**
 * Created by nabillasabbaha on 24/09/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class AnnouncementOvoPresenterTest {

    @Mock
    IntroOvoContract.View view;
    @Mock
    CheckNumberOvoUseCase checkNumberOvoUseCase;

    private IntroOvoPresenter presenter;
    private CheckPhoneOvoModel checkRegisteredPhoneOvoModel;
    private CheckPhoneOvoModel checkNotRegisteredPhoneOvoModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new IntroOvoPresenter(checkNumberOvoUseCase, new WalletTestScheduler());
        presenter.attachView(view);
    }

    @Test
    public void checkPhoneNumber_UserRegistered_Success() {
        //given
        setCheckRegisteredNumberModel();
        Mockito.when(checkNumberOvoUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.just(checkRegisteredPhoneOvoModel));
        //when
        presenter.checkPhoneNumber();

        //then
        Mockito.verify(view).directPageWithApplink(checkRegisteredPhoneOvoModel.getRegisteredApplink());

    }

    @Test
    public void checkPhoneNumber_UserUnregistered_Success() {
        //given
        setCheckNotRegisteredNumberModel();
        Mockito.when(checkNumberOvoUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.just(checkNotRegisteredPhoneOvoModel));
        //when
        presenter.checkPhoneNumber();

        //then
        Mockito.verify(view).directPageWithApplink(checkNotRegisteredPhoneOvoModel.getNotRegisteredApplink());

    }

    @Test
    public void checkPhoneNumber_UserNotYetHavePhoneNumber_Success() {
//        //given
//        setCheckNumberModel();
//        Mockito.when(checkNumberOvoUseCase.createObservable(Mockito.anyObject()))
//                .thenReturn(Observable.just(setCheckNumberModel()));
//        //when
//        presenter.checkPhoneNumber();
//
//        //then
//        Mockito.verify(view).directPageToOtpPage();

    }

    @Test
    public void checkPhoneNumber_UserRegistered_Failed() {
        //given
        String message = "Error";
        Mockito.when(checkNumberOvoUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.error(new MessageErrorException(message)));
        Mockito.when(view.getErrorMessage(new Throwable())).thenReturn(message);
        //when
        presenter.checkPhoneNumber();

        //then
        Mockito.verify(view).showSnackbarErrorMessage(Mockito.anyObject());
    }



    private void setCheckRegisteredNumberModel() {
        checkRegisteredPhoneOvoModel = new CheckPhoneOvoModel();
        checkRegisteredPhoneOvoModel.setPhoneNumber("628572257969");
        checkRegisteredPhoneOvoModel.setRegistered(true);
        checkRegisteredPhoneOvoModel.setRegisteredApplink("tokopedia://webview?url=https://www.tokopedia.com/api/v1/activate");
        checkRegisteredPhoneOvoModel.setNotRegisteredApplink("tokopedia://ovo/activation");
        checkRegisteredPhoneOvoModel.setChangeMsisdnApplink("https://m.tokopedia.com/user/profile/edit");
    }

    private void setCheckNotRegisteredNumberModel() {
        checkNotRegisteredPhoneOvoModel = new CheckPhoneOvoModel();
        checkNotRegisteredPhoneOvoModel.setPhoneNumber("628572257969");
        checkNotRegisteredPhoneOvoModel.setRegistered(false);
        checkNotRegisteredPhoneOvoModel.setRegisteredApplink("tokopedia://webview?url=https://www.tokopedia.com/api/v1/activate");
        checkNotRegisteredPhoneOvoModel.setNotRegisteredApplink("tokopedia://ovo/activation");
        checkNotRegisteredPhoneOvoModel.setChangeMsisdnApplink("https://m.tokopedia.com/user/profile/edit");
    }
}