package com.tokopedia.flight.searchV2.presentation.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchCombinedApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;

/**
 * Created by Rizky on 26/09/18.
 */
public class FlightSearchTestingContract {

    public interface View extends CustomerView {

    }

    public interface Presenter extends CustomerPresenter<View> {

        void searchFlight(FlightSearchApiRequestModel flightSearchApiRequestModel, FlightSearchCombinedApiRequestModel flightSearchCombinedApiRequestModel, boolean oneWay, boolean isReturning);

    }

}
