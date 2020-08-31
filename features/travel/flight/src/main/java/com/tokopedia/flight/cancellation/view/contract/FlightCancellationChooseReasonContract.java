package com.tokopedia.flight.cancellation.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonModel;

import java.util.ArrayList;

/**
 * @author by furqan on 30/10/18.
 */

public class FlightCancellationChooseReasonContract {

    public interface View extends CustomerView {

        void renderReasonList(ArrayList<FlightCancellationReasonModel> reasonViewModelList);

    }

    public interface Presenter {

        void getReasonList();

    }

}
