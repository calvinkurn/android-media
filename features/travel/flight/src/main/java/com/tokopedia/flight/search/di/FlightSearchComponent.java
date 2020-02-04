package com.tokopedia.flight.search.di;

import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.search.presentation.activity.FlightSearchFilterActivity;
import com.tokopedia.flight.search.presentation.fragment.FlightSearchFragment;
import com.tokopedia.flight.search.presentation.fragment.FlightSearchReturnFragment;

import dagger.Component;

/**
 * @author by furqan on 01/10/18.
 */

@FlightSearchScope
@Component(modules = FlightSearchModule.class, dependencies = FlightComponent.class)
public interface FlightSearchComponent {

    FlightApi flightApi();

    void inject(FlightSearchFilterActivity flightSearchFilterActivity);

    void inject(FlightSearchFragment flightSearchFragment);

    void inject(FlightSearchReturnFragment flightSearchReturnFragment);
}