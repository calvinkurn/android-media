package com.tokopedia.flight.cancellation.view.contract;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperModel;

/**
 * Created by alvarisi on 4/9/18.
 */

public interface FlightCancellationRefundDetailContract {
    interface View extends CustomerView{

        void showLoading();

        FlightCancellationWrapperModel getCancellationViewModel();

        void hideLoading();

        Activity getActivity();

        void showErrorFetchEstimateRefund(String message);

        void renderTotalRefund(String totalRefund);

        void showFullPageContainer();

        void hideFullPageContainer();
    }

    interface Presenter extends CustomerPresenter<View>{

        void initialize();

        void onRetryFetchEstimate();

        void onNextButtonClicked();
    }
}
