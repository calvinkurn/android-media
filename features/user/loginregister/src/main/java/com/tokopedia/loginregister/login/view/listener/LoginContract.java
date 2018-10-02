package com.tokopedia.loginregister.login.view.listener;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.loginregister.common.data.DiscoverItemViewModel;
import com.tokopedia.loginregister.common.data.GetUserInfoDomainData;
import com.tokopedia.loginregister.common.data.SecurityDomain;
import com.tokopedia.loginregister.common.view.GetFacebookCredentialSubscriber;

import java.util.ArrayList;

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

        void setAutoCompleteAdapter(ArrayList<String> listId);

        void showLoadingDiscover();

        void dismissLoadingDiscover();

        void onErrorDiscoverLogin(String errorMessage);

        void onSuccessDiscoverLogin(ArrayList<DiscoverItemViewModel> providers);

        GetFacebookCredentialSubscriber.GetFacebookCredentialListener getFacebookCredentialListener();

        void onGoToCreatePasswordPage(GetUserInfoDomainData getUserInfoDomainData);

        void onGoToPhoneVerification();

        void onGoToSecurityQuestion(SecurityDomain securityDomain, String fullName, String email, String phone);

        void setSmartLock();

        void onErrorLogin(String errorMessage, int codeError);

        void onGoToActivationPage(String email);

        void onSuccessLoginEmail();

        void onSuccessLoginSosmed(String loginMethod);

        Context getContext();

        void disableArrow();

        void enableArrow();

        void onForbidden();

        void onGoToAddName(GetUserInfoDomainData getUserInfoDomainData);

        boolean isFromRegister();
    }

    interface Presenter extends CustomerPresenter<View> {

        void login(String email, String password);

        void saveLoginEmail(String email);

        ArrayList<String> getLoginIdList();

        void discoverLogin();

        void loginWebview(Intent data);

        void loginGoogle(String accessToken, String email);

        void getFacebookCredential(Fragment fragment, CallbackManager callbackManager);

        void loginFacebook(AccessToken accessToken, String email);

    }
}