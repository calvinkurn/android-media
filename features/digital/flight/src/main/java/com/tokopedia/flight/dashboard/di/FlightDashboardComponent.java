package com.tokopedia.flight.dashboard.di;

import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.dashboard.view.fragment.FlightClassesfragment;
import com.tokopedia.flight.dashboard.view.fragment.FlightDashboardFragment;
import com.tokopedia.flight.dashboard.view.fragment.FlightSelectPassengerFragment;
import com.tokopedia.flight.dashboard.view.widget.FlightCalendarOneWayWidget;

import dagger.Component;

/**
 * Created by alvarisi on 10/26/17.
 */
@FlightDashboardScope
@Component(modules = {FlightDashboardModule.class, FlightDashboardViewModelModule.class},
        dependencies = FlightComponent.class)
public interface FlightDashboardComponent {
    FlightDateUtil flightdateutlil();

    void inject(FlightSelectPassengerFragment flightSelectPassengerFragment);

    void inject(FlightClassesfragment flightClassesfragment);

    void inject(FlightDashboardFragment flightDashboardFragment);

    void inject(FlightCalendarOneWayWidget flightCalendarOneWayWidget);
}
