package com.tokopedia.loginregister.login.view.listener;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel;
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber;
import com.tokopedia.sessioncommon.data.LoginTokenPojo;
import com.tokopedia.sessioncommon.data.profile.ProfilePojo;
import com.tokopedia.sessioncommon.view.LoginSuccessRouter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

/**
 * @author by nisie on 10/2/18.
 */

public interface LoginContract {

    interface View extends CustomerView {

        void resetError();

        void showLoadingLogin();

        void showErrorPassword(int resId);

        void showErrorEmail(int resId);

        void dismissLoadingLogin();

        void onSuccessLogin();

        void onErrorLogin(String errorMessage);

        void showLoadingDiscover();

        void dismissLoadingDiscover();

        void onErrorDiscoverLogin(String errorMessage);

        void onSuccessDiscoverLogin(ArrayList<DiscoverItemViewModel> providers);

        GetFacebookCredentialSubscriber.GetFacebookCredentialListener getFacebookCredentialListener();

        void onSuccessLoginSosmed(String loginMethod);

        Context getContext();

        void onGoToAddName();

        boolean isFromRegister();

        void setSmartLock();

        void onErrorLoginSosmed(String loginMethodName, String errorCodeMessage);

        void onSuccessLoginEmail();

        void stopTrace();

        void trackErrorLoginEmail();

        @NotNull
        Function1<LoginTokenPojo, Unit> onSuccessLoginToken(String email);

        @NotNull
        Function1<Throwable, Unit> onErrorLoginEmail(String email);

        @NotNull
        Function1<LoginTokenPojo, Unit> onSuccessReloginAfterSQ();

        @NotNull
        Function1<Throwable, Unit> onErrorReloginAfterSQ(String validateToken);

        @NotNull
        Function1<LoginTokenPojo, Unit> onSuccessLoginFacebook(@NotNull String email);

        @NotNull
        Function1<Throwable, Unit> onErrorLoginFacebook(@NotNull String email);

        @NotNull
        Function1<LoginTokenPojo, Unit> onSuccessLoginGoogle(@Nullable String email);

        @NotNull
        Function1<Throwable, Unit> onErrorLoginGoogle(@Nullable String email);

        @NotNull
        Function1<ProfilePojo, Unit> onSuccessGetUserInfo();

        @NotNull
        Function1<Throwable, Unit> onErrorGetUserInfo();

        @NotNull
        Function2<String, String, Unit> onGoToCreatePassword();

        @NotNull
        Function0<Unit> onGoToPhoneVerification();

        @NotNull
        Function1<MessageErrorException, Unit> onGoToActivationPage(String email);

        @NotNull
        Function0<Unit> onGoToSecurityQuestion(String email);

        @NotNull
        Function1<MessageErrorException, Unit> onGoToActivationPageAfterRelogin();

        @NotNull
        Function0<Unit> onGoToSecurityQuestionAfterRelogin();

        void onGoToForbiddenPage();

    }

    interface Presenter extends CustomerPresenter<View> {

        void loginEmail(String email, String password);

        void loginWebview(Intent data);

        void loginGoogle(String accessToken, String email);

        void getFacebookCredential(Fragment fragment, CallbackManager callbackManager);

        void loginFacebook(AccessToken accessToken, String email);

        void getUserInfo();
    }
}