package com.tokopedia.flight.searchV2.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.searchV2.presentation.contract.FlightSearchReturnContract;
import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * @author by furqan on 15/10/18.
 */

public class FlightSearchReturnPresenter extends BaseDaggerPresenter<FlightSearchReturnContract.View>
        implements FlightSearchReturnContract.Presenter {

    private static final long ONE_HOUR = TimeUnit.HOURS.toMillis(1);
    private CompositeSubscription compositeSubscription;

    @Inject
    public FlightSearchReturnPresenter() {
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onFlightSearchSelected(String selectedFlightDeparture, FlightJourneyViewModel journeyViewModel) {

    }

    @Override
    public void onFlightSearchSelected(String selectedFlightDeparture, String selectedFlightReturn) {

    }

    @Override
    public void onFlightSearchSelected(String selectedFlightDeparture, FlightJourneyViewModel journeyViewModel, int adapterPosition) {

    }

    @Override
    public void onDestroy() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
    }
}
