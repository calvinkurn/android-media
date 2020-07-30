package com.tokopedia.flight.cancellation.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListModel;

/**
 * @author by furqan on 03/05/18.
 */

public interface FlightCancellationDetailContract {
    interface View extends CustomerView {

        FlightCancellationListModel getFlightCancellationList();

    }

    interface Presenter {

        void onViewCreated();

    }
}
