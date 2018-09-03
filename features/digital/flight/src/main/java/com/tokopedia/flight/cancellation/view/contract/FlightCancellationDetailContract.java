package com.tokopedia.flight.cancellation.view.contract;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;

/**
 * @author by furqan on 03/05/18.
 */

public interface FlightCancellationDetailContract {
    interface View extends CustomerView {

        FlightCancellationListViewModel getFlightCancellationList();

        void renderCancellationStatus(@StringRes int resId);

    }

    interface Presenter {

        void onViewCreated();

        void checkCancellationStatus();

    }
}
