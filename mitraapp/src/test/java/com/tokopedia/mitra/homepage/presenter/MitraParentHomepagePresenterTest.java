package com.tokopedia.mitra.homepage.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.homepage.contract.MitraParentHomepageContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MitraParentHomepagePresenterTest {
    MitraParentHomepageContract.Presenter presenter;

    @Mock
    UserSession userSession;

    @Mock
    MitraParentHomepageContract.View view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new MitraParentHomepagePresenter(userSession);
        presenter.attachView(view);
    }

    @Test
    public void onHomepageMenuClicked_anyLoginState_inflateHomepageAndSelectHomeMenu() {
        //given
        when(userSession.isLoggedIn()).thenReturn(false);

        //when
        presenter.onHomepageMenuClicked();

        //then
        verify(view).inflateHomepageFragment();
        verify(view).setHomepageMenuSelected();
    }

    @Test
    public void onAccountMenuClicked_loggedIn_inflateAccountAndSelectAccountMenu() {
        //given
        when(userSession.isLoggedIn()).thenReturn(true);

        //when
        presenter.onAccountMenuClicked();

        //then
        verify(view).inflateAccountFragment();
        verify(view).setAccountMenuSelected();
    }

    @Test
    public void onAccountMenuClicked_notLoggedIn_inflateAccountAndSelectAccountMenu() {
        //given
        when(userSession.isLoggedIn()).thenReturn(false);

        //when
        presenter.onAccountMenuClicked();

        //then
        verify(view).navigateToLoggedInThenAccountPage();
    }

    @Test
    public void onLoginFromAccountResultReceived_notLoggedIn_showErrorMessage() {
        //given
        when(userSession.isLoggedIn()).thenReturn(false);

        //when
        presenter.onLoginFromAccountResultReceived();

        //then
        verify(view).showErrorMessageInSnackbar(R.string.mitra_homepage_login_failed_error_message);
    }

    @Test
    public void onLoginFromAccountResultReceived_loggedIn_inflateAccountAndSelectAccountMenu() {
        //given
        when(userSession.isLoggedIn()).thenReturn(true);

        //when
        presenter.onLoginFromAccountResultReceived();

        //then
        verify(view).inflateAccountFragment();
        verify(view).setAccountMenuSelected();
    }
}