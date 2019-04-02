package com.tokopedia.flight.cancellation.view.contract;

import android.app.Activity;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;

/**
 * @author by furqan on 11/04/18.
 */

public interface FlightCancellationReviewContract {
    interface View extends CustomerView {

        void showSuccessDialog(@StringRes int resId);

        void showLoading();

        void hideLoading();

        void showEstimateValue();

        void hideEstimateValue();

        void showRefundDetail(int resId);

        void hideRefundDetail();

        String getInvoiceId();

        FlightCancellationWrapperViewModel getCancellationWrapperViewModel();

        void setCancellationWrapperViewModel(FlightCancellationWrapperViewModel viewModel);

        void showCancellationError(Throwable throwable);

        Activity getActivity();

        void showErrorFetchEstimateRefund(String messageFromException);

        void renderView();
    }

    interface Presenter {

        void requestCancellation();

        void onViewCreated();

        void onRetryFetchEstimate();
    }
}
