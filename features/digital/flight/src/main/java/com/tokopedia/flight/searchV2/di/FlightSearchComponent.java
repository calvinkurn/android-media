package com.tokopedia.flight.searchV2.di;

import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.searchV2.presentation.activity.FlightSearchFilterActivity;
import com.tokopedia.flight.searchV2.presentation.fragment.FlightSearchFragment;
import com.tokopedia.flight.searchV2.presentation.fragment.FlightSearchReturnFragment;

import dagger.Component;

/**
 * @author by furqan on 01/10/18.
 */

@FlightSearchScope
@Component(modules = FlightSearchModule.class, dependencies = FlightComponent.class)
public interface FlightSearchComponent {

    FlightModuleRouter flightModuleRouter();

    FlightApi flightApi();

    void inject(FlightSearchFragment flightSearchFragment);

    void inject(FlightSearchReturnFragment flightSearchReturnFragment);

    void inject(FlightSearchFilterActivity flightSearchFilterActivity);

}