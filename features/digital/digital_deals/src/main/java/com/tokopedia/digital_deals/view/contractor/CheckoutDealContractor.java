package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.PackageViewModel;
import com.tokopedia.usecase.RequestParams;


public class CheckoutDealContractor {

    public interface View extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderFromPackageVM(DealsDetailsViewModel dealDetails, PackageViewModel packageViewModel);

        void showProgressBar();

        void hideProgressBar();


        void setEmailID(String emailID);

        RequestParams getParams();

        android.view.View getRootView();

        void showPromoSuccessMessage(String text, int color);

        void showCashbackMessage(String text);

        void hideSuccessMessage();

        boolean validateAllFields();
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
