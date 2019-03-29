package com.tokopedia.posapp.auth.login.view;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;
import com.tokopedia.session.data.viewmodel.SecurityDomain;

import java.util.ArrayList;

/**
 * @author by nisie on 12/18/17.
 */

public interface Login {

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
}
