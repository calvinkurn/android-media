package com.tokopedia.loginregister.registerinitial.view.listener;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.loginregister.common.data.GetUserInfoDomainData;
import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel;
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber;

import java.util.ArrayList;

/**
 * @author by nisie on 10/24/18.
 */
public interface RegisterContract {

    interface View extends CustomerView {

        void showLoadingDiscover();

        void onErrorDiscoverRegister(String errorMessage);

        void onSuccessDiscoverRegister(ArrayList<DiscoverItemViewModel> discoverViewModel);

        void dismissLoadingDiscover();

        void showProgressBar();

        void dismissProgressBar();

        void onErrorRegisterSosmed(String errorMessage);

        GetFacebookCredentialSubscriber.GetFacebookCredentialListener getFacebookCredentialListener();

        void onForbidden();

        void showRegisteredEmailDialog(String email);

        void showRegisteredPhoneDialog(String phone);

        void showProceedWithPhoneDialog(String phone);

        void goToLoginPage();

        void goToRegisterEmailPageWithEmail(String email);

        void onErrorValidateRegister(String message);

        void setTempPhoneNumber(String maskedPhoneNumber);


        Context getContext();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getProvider();

        void registerWebview(Intent data);

        void getFacebookCredential(Fragment fragment, CallbackManager callbackManager);

        void registerFacebook(AccessToken accessToken);

        void registerGoogle(String model);

        void validateRegister(String id);
    }

}