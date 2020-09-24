package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Context;
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

        android.view.View getRootView();

        void showPromoSuccessMessage(String text, String message, long discountAmount, Boolean isCancel);

        void updateAmount(String s);

        void showFailureMessage(String error);

    }

    public interface Presenter extends CustomerPresenter<View> {

        void onDestroy();

        void updateEmail(String email);

        void updatePromoCode(String code);

        void clickGoToPromo(Context context);

        void clickGoToDetailPromo(Context context, String couponCode);

        void clickGotToListPromoApplied(Context context, String promoCode);

    }
}
