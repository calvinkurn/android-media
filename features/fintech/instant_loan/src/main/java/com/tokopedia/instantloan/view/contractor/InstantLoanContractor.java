package com.tokopedia.instantloan.view.contractor;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.instantloan.data.model.response.PhoneDataEntity;
import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity;

public interface InstantLoanContractor {

    interface View extends CustomerView {
        Context getAppContext();

        Context getActivityContext();

        void onSuccessLoanProfileStatus(UserProfileLoanEntity status);

        void setUserOnGoingLoanStatus(boolean status, int loanId);

        void onErrorLoanProfileStatus(String onErrorLoanProfileStatus);

        void onSuccessPhoneDataUploaded(PhoneDataEntity data);

        void onErrorPhoneDataUploaded(String errorMessage);

        void navigateToLoginPage();

        void startIntroSlider();

        void showToastMessage(String message, int duration);

        void openWebView(String url);

        void searchLoanOnline();

        void showLoader();

        void hideLoader();

        void showLoaderIntroDialog();

        void hideLoaderIntroDialog();

        void hideIntroDialog();
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();

        void getLoanProfileStatus();

        void postPhoneData(String userId);

        boolean isUserLoggedIn();

        void startDataCollection();
    }
}
