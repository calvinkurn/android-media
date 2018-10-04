package com.tokopedia.flight.searchV2.di;

import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.searchV2.presentation.view.FlightSearchTestingFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 10/24/17.
 */
@FlightSearchV2Scope
@Component(modules = FlightSearchV2Module.class, dependencies = FlightComponent.class)
public interface FlightSearchV2Component {

    FlightApi flightApi();

    FlightModuleRouter flightModuleRouter();

    void inject(FlightSearchTestingFragment flightSearchFragment);

}
