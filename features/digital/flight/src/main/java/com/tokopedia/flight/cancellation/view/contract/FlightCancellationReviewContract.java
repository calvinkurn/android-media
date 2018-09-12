package com.tokopedia.flight.cancellation.view.contract;

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

        String getInvoiceId();

        FlightCancellationWrapperViewModel getCancellationWrapperViewModel();

        void setCancellationWrapperViewModel(FlightCancellationWrapperViewModel viewModel);

        void showCancellationError(Throwable throwable);
    }

    interface Presenter {

        void requestCancellation();

        void onViewCreated();

    }
}
