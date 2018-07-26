package com.tokopedia.tkpd.flight.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.flight.FlightGetProfileInfoData;
import com.tokopedia.tkpd.flight.di.module.FlightConsumerModule;

import dagger.Component;

/**
 * @author by alvarisi on 1/24/18.
 */
@FlightConsumerScope
@Component(modules = FlightConsumerModule.class, dependencies = AppComponent.class)
public interface FlightConsumerComponent {

    void inject(FlightGetProfileInfoData flightGetProfileInfoData);
}
