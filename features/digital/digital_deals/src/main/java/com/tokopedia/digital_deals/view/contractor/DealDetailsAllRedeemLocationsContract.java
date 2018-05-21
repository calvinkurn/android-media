package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.viewmodel.OutletViewModel;

import java.util.List;


public class DealDetailsAllRedeemLocationsContract {

        public interface View extends CustomerView {

            Activity getActivity();

            void navigateToActivityRequest(Intent intent, int requestCode);

            void renderBrandDetails(List<OutletViewModel> outletViewModelList);

            void showProgressBar();

            void hideProgressBar();


        }
        public interface Presenter extends CustomerPresenter<View> {

            void initialize(List<OutletViewModel> outletViewModelList);

            void onDestroy();
        }
    }
