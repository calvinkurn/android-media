package com.tokopedia.flight.cancellation.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListModel;

import java.util.List;

/**
 * @author by furqan on 30/04/18.
 */

public interface FlightCancellationListContract {

    interface View extends CustomerView {

        void navigateToDetailPage(FlightCancellationListModel passData);

        void renderList();

        List<FlightCancellationListModel> getFlightCancellationList();

        String getInvoiceId();

        void setFlightCancellationList(List<FlightCancellationListModel> cancellationList);

    }

    interface Presenter {
        void onViewCreated();
    }

}
