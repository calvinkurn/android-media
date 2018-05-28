package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.usecase.RequestParams;


public class SelectQuantityContract {

    public interface View extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void navigateToActivity(Intent intent);

        void renderFromDetails(DealsDetailsViewModel dealDetail);

        RequestParams getParams();

        void showPayButton();

        void hidePayButton();

        void showProgressBar();

        void hideProgressBar();

        android.view.View getRootView();

        int getRequestCode();

    }

    public interface Presenter extends CustomerPresenter<SelectQuantityContract.View> {

        void initialize(DealsDetailsViewModel detailsViewModel);

        void onDestroy();

        void onActivityResult(int requestCode);

    }
}
