package com.tokopedia.mitra.homepage.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.RxAndroidTestPlugins;
import com.tokopedia.mitra.RxJavaTestPlugins;
import com.tokopedia.mitra.homepage.contract.MitraHomepageContract;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class MitraHomepagePresenterTest {
    @Mock
    MitraHomepageContract.View view;
    @Mock
    UserSession userSession;


    MitraHomepagePresenter presenter;

    @Before
    public void setUp() throws Exception {
        RxJavaTestPlugins.setImmediateScheduler();
        RxAndroidTestPlugins.setImmediateScheduler();
        MockitoAnnotations.initMocks(this);
        presenter = new MitraHomepagePresenter(userSession);
        presenter.attachView(view);
    }

    @After
    public void tearDown() throws Exception {
        RxJavaTestPlugins.resetJavaTestPlugins();
        RxAndroidTestPlugins.resetAndroidTestPlugins();
    }

    @Test
    public void onViewCreated_unloggedInUser_showTopLoginContainer() {
        //given
        when(userSession.isLoggedIn()).thenReturn(false);
        //when
        presenter.onViewCreated();
        //then
        verify(view).showLoginContainer();
    }

    @Test
    public void onViewCreated_loggedInUser_hideTopLoginContainer() {
        //given
        when(userSession.isLoggedIn()).thenReturn(true);
        //when
        presenter.onViewCreated();
        //then
        verify(view).hideLoginContainer();
    }

    @Test
    public void onLoginResultReceived_loginsuccess_hideLoginContainer() {
        //given
        when(userSession.isLoggedIn()).thenReturn(true);

        //given
        presenter.onLoginResultReceived();

        //then
        verify(view).hideLoginContainer();
    }

    @Test
    public void onLoginResultReceived_loginsuccess_fetchTheAgentStatus() {
        //given
        when(userSession.isLoggedIn()).thenReturn(true);

        //when
        presenter.onLoginResultReceived();

        //then
        //TODO : verify get agen
    }

    @Test
    public void onLoginResultReceived_loginFailed_showFailedErrorMessage() {
        //given
        when(userSession.isLoggedIn()).thenReturn(false);
        //when
        presenter.onLoginResultReceived();
        //then
        verify(view).showMessageInRedSnackBar(R.string.mitra_homepage_login_failed_error_message);
    }

    @Test
    public void onLoginBtnClicked_loginBtnClick_navigateToLogin() {
        verify(view).navigateToLoginPage();
    }
}