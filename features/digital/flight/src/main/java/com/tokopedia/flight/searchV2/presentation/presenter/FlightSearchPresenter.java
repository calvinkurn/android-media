package com.tokopedia.flight.searchV2.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.searchV2.presentation.contract.FlightSearchContract;
import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel;

import javax.inject.Inject;

/**
 * @author by furqan on 01/10/18.
 */

public class FlightSearchPresenter extends BaseDaggerPresenter<FlightSearchContract.View>
        implements FlightSearchContract.Presenter{

    @Inject
    public FlightSearchPresenter() {
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onSeeDetailItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {

    }

    @Override
    public void onSearchItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {

    }

    @Override
    public void onSearchItemClicked(FlightJourneyViewModel journeyViewModel) {

    }

    @Override
    public void onSuccessDateChanged(int year, int month, int dayOfMonth) {

    }

    @Override
    public void setDelayHorizontalProgress() {

    }
}
