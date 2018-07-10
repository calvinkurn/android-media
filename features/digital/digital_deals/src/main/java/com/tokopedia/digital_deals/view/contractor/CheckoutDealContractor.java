package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.model.PackageViewModel;
import com.tokopedia.usecase.RequestParams;


public class CheckoutDealContractor {

    public interface View extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderFromDetails(DealsDetailsResponse dealDetails, PackageViewModel packageViewModel);

        void showProgressBar();

        void hideProgressBar();

        void setEmailIDPhoneNumber(String emailID, String phoneNumber);

        RequestParams getParams();

        android.view.View getRootView();

        void showPromoSuccessMessage(String text, String message, long discountAmount);

        void showCashbackMessage(String text);

        void hideSuccessMessage();

        void updateAmount(String s);
    }

    public interface Presenter extends CustomerPresenter<View> {

        void onDestroy();

        void getProfile();

        void updateEmail(String email);

        void updatePromoCode(String code);

        void clickGoToPromo();

        public String getSCREEN_NAME();
    }
}
