package com.tokopedia.mitra.account.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.logout.domain.model.LogoutDomain;
import com.tokopedia.logout.domain.usecase.LogoutUseCase;
import com.tokopedia.mitra.RxAndroidTestPlugins;
import com.tokopedia.mitra.RxJavaTestPlugins;
import com.tokopedia.mitra.account.contract.MitraAccountContract;
import com.tokopedia.usecase.RequestParams;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MitraAccountPresenterTest {
    private static final String NAME = "Steve Roger";
    private static final String PHONE = "+(62) 85747212168";
    private MitraAccountContract.Presenter presenter;

    @Mock
    UserSession userSession;
    @Mock
    com.tokopedia.user.session.UserSession sessionUserSession;
    @Mock
    LogoutUseCase logoutUseCase;
    @Mock
    MitraAccountContract.View view;

    @Before
    public void setUp() throws Exception {
        RxJavaTestPlugins.setImmediateScheduler();
        RxAndroidTestPlugins.setImmediateScheduler();
        MockitoAnnotations.initMocks(this);
        presenter = new MitraAccountPresenter(userSession, sessionUserSession, logoutUseCase);
        presenter.attachView(view);

    }


    @After
    public void tearDown() throws Exception {
        RxJavaTestPlugins.resetJavaTestPlugins();
        RxAndroidTestPlugins.resetAndroidTestPlugins();
    }


    @Test
    public void onViewCreated_nameNotNull_renderName() {
        //given
        when(userSession.getName()).thenReturn(NAME);
        //when
        presenter.onViewCreated();
        //then
        verify(view).renderName(NAME);
    }

    @Test
    public void onViewCreated_phoneNumberNotNull_renderPhoneNumber() {
        //given
        when(userSession.getPhoneNumber()).thenReturn(PHONE);
        //when
        presenter.onViewCreated();
        //then
        verify(view).renderPhoneNumber(PHONE);
    }

    @Test
    public void onLogoutClicked() {
        //when
        presenter.onLogoutClicked();

        //then
        verify(view).showLogoutConfirmationDialog();
    }

    @Test
    public void onLogoutConfirmed() {

        LogoutDomain logoutDomain = new LogoutDomain(false);
        when(logoutUseCase.createObservable(anyObject())).thenReturn(
                rx.Observable.just(logoutDomain)
        );

        //when
        presenter.onLogoutConfirmed();

        //then
        //HIT LOGOUT
        verify(view, times(0)).navigateToLogin();
    }
}