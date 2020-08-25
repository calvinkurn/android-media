package com.tokopedia.flight.cancellation.view.contract;

import android.app.Activity;

import androidx.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperModel;

import java.util.List;

/**
 * @author by furqan on 11/04/18.
 */

public interface FlightCancellationReviewContract {
    interface View extends CustomerView {

        void showSuccessDialog(@StringRes int resId);

        void showLoading();

        void hideLoading();

        void showEstimateValue(List<String> estimationNotes);

        void hideEstimateValue();

        void showRefundDetail(int resId);

        void hideRefundDetail();

        String getInvoiceId();

        FlightCancellationWrapperModel getCancellationWrapperViewModel();

        void setCancellationWrapperViewModel(FlightCancellationWrapperModel viewModel);

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
