package com.tokopedia.flight.searchV2.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.searchV2.presentation.contract.FlightSearchContract;

import javax.inject.Inject;

/**
 * @author by furqan on 01/10/18.
 */

public class FlightSearchPresenter extends BaseDaggerPresenter<FlightSearchContract.View>
        implements FlightSearchContract.Presenter{

    @Inject
    public FlightSearchPresenter() {
    }
}
