package com.tokopedia.instantloan.view.contractor;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.instantloan.view.model.LoanProfileStatusViewModel;
import com.tokopedia.instantloan.view.model.PhoneDataViewModel;

/**
 * Created by lavekush on 21/03/18.
 */

public interface InstantLoanContractor {

    interface View extends CustomerView {
        Context getAppContext();

        Context getActivityContext();

        void onSuccessLoanProfileStatus(LoanProfileStatusViewModel status);

        void onErrorLoanProfileStatus(String onErrorLoanProfileStatus);

        void onSuccessPhoneDataUploaded(PhoneDataViewModel data);

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
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();

        void getLoanProfileStatus();

        void postPhoneData(String userId);

        boolean isUserLoggedIn();

        void startDataCollection();
    }
}
